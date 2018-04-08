package obhs.com.paperlessfeedback.Network;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.DashboardFragments.DashboardFragment;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

/**
 * Created by 1018651 on 04/03/2018.
 */

public class CloudConnection extends AsyncTask<FeedbackObj, Void, Integer> {

//    private FeedbackObj feedbackObj;
//    private DashboardFragment liveDashboardFragment;
    private GlobalContext globalContext;

    public CloudConnection(GlobalContext globalContext) {
//    public CloudConnection(FeedbackObj feedbackObj, DashboardFragment f) {
//        this.feedbackObj = feedbackObj;
//        liveDashboardFragment = f;
        this.globalContext = globalContext;
    }

    @Override
    protected Integer doInBackground(FeedbackObj... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
//        GlobalContext globalContext = params[0];
        FeedbackObj feedbackObj = params[0];
        AppDatabase db = globalContext.getDb();

        //insert in local db, then send to server
        if(feedbackObj != null) {
            //if feedbackObj is not null, then insert before syncing
            //edit: hackish code
            db.feedbackObjDao().insertAll(feedbackObj);
        }

        //read all pending entries from db
        List<FeedbackObj> feedbackObjs = db.feedbackObjDao().getAll();
        Log.d("debugTag", "count of entries: " + feedbackObjs.size());

        //String POST_PARAMS = "PSI=120&CoachNumber=S1&SeatNumber=40&MobileNumber=8008713000&TrainNumber=1234&Date=2018-04-01&TripID=201804011238&TrainStartDate=2018-04-01&PNRNumber=1018651";

        try {
             /*
             URL url = new URL("https://paperlessfeedbackautomation.appspot.com/Feedback");
             */

            int i = 0;
            //edit: review loop
            for(FeedbackObj currentFeedbackObj : feedbackObjs) {
                Log.d("debugTag", "here again" + ++i);

                URL url = new URL("http://paperlessfeedbackautomation.appspot.com/Feedback");
                connection = (HttpURLConnection) url.openConnection();
                //connection.connect();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();

                ///Adding Image
//                Bitmap faceImage =  currentFeedbackObj.getFaceImage();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                faceImage.compress(Bitmap.CompressFormat.JPEG, 60, baos);
//                byte[] byteArray = baos.toByteArray();
                byte[] byteArray = currentFeedbackObj.getFaceImage();
                String encoded = Base64.encodeToString(byteArray, Base64.URL_SAFE);

                String queryString = currentFeedbackObj.getQueryString() +"&photo="+ encoded;
                os.write(queryString.getBytes());
                //os.write(currentFeedbackObj.getQueryString().getBytes());
                os.flush();

                //edit: remove when success from server
                db.feedbackObjDao().delete(currentFeedbackObj);

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                Log.d("debugTag: ", "server reply: " + finalJson);
                os.close();
                connection.disconnect();
            }

        }
        catch (Exception e)
        {
            Log.d("Exception","Exception"+e.getStackTrace());

        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update view and global context on remaining entries
        int  numEntries = globalContext.getDb().feedbackObjDao().countFeedbackObj();
        return numEntries;
    }

    @Override
    protected void onPostExecute(Integer n) {
        globalContext.setNumLocalDbFeedbacks(n);
        globalContext.getLiveDashboardFragment().setNumEntriesLocal(n);
        if(globalContext.getCurrentTrip().getTripStatus() == Trip.TripStatus.GOING) {
            boolean enable = (n == 0)?true:false;
            globalContext.getLiveDashboardFragment().setEndTripButtonEnabled(enable);
        }
    }
}
