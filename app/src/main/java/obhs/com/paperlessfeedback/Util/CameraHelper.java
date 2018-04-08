package obhs.com.paperlessfeedback.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import obhs.com.paperlessfeedback.Network.CloudConnection;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 1018651 on 04/03/2018.
 */

public class CameraHelper {
    private Camera camera;
    private int cameraId = 0;
    FeedbackObj feedbackObj;

    public CameraHelper(FeedbackObj feedbackObj)
    {
        this.feedbackObj = feedbackObj;
    }

    public void ShootFace(Context context)
    {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(context,"No camera on this device", Toast.LENGTH_LONG).show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(context, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                safeCameraOpen(cameraId);
            }
            SurfaceView view = new SurfaceView(context);
            if(camera!=null)
            {
                try {
                    camera.setPreviewDisplay(view.getHolder());
                    SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
                    camera.setPreviewTexture(st);
                    camera.startPreview();
                } catch (IOException e) {
                    Log.v("MyActivity", "Camera preview is fucked up");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Camera.Parameters params = camera.getParameters();
                params.setJpegQuality(100);
                camera.setParameters(params);
                camera.takePicture(null, null, mCall);
            }
            else
            {
                Log.v("MyActivity", "Camera object is null, may be permission issue");
            }
        }
        Log.v("CameraHelper", "Returning Image");
    }

    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //decode the data obtained by the camera into a Bitmap
            //display.setImageBitmap(photo);
            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
            //display.setImageBitmap(bitmapPicture);
            Log.v("CameraHelper", "Bitmap Captured");
            feedbackObj.setFaceImage(image);
            new CloudConnection().execute(feedbackObj);
            releaseCamera();
        }
    };
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.v("MyActivity", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;
        try {
            releaseCamera();
            camera = Camera.open(id);
            qOpened = (camera != null);
        } catch (Exception e) {
            Log.e("CameraHelper", "failed to open Camera");
            e.printStackTrace();
        }
        return qOpened;
    }
    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
