package obhs.com.paperlessfeedback.Network;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import obhs.com.paperlessfeedback.FeedbackFragments.FeedbackFormFragment;
import obhs.com.paperlessfeedback.FeedbackFragments.PassengerVeificationFragment;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.Util.Util;

import static obhs.com.paperlessfeedback.Util.Util.logd;

/**
 * Created by 1018651 on 04/09/2018.
 */


public class PNRStatusCheck extends AsyncTask<String, Void , Boolean> {

    Fragment veificationFragment;
    public PNRStatusCheck(Fragment veificationFragment)
    {
        this.veificationFragment = veificationFragment;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String pnrNumber = strings[0];
        String seatNumber = strings[1];
        Boolean isValid = true;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            Log.d("PNR", " status check");

            URL url = new URL("http://api.railwayapi.com/v2/pnr-status/pnr/"+pnrNumber+"/apikey/xifwvh1pdu");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.connect();

           // connection.setDoOutput(true);


            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonObj = new JSONObject(response.toString());
                String PNRResponseCode = jsonObj.getString("response_code");
                Log.d("PNR: ", "response_code : " + PNRResponseCode);
                if(PNRResponseCode.equals("404") || PNRResponseCode.equals("405"))
                {
                    isValid = false;
                }
                else
                if(PNRResponseCode.equals("200"))
                {
                    JSONArray passengerArray = jsonObj.getJSONArray("passengers");
                    String bookingString = passengerArray.toString();
                    if(!bookingString.contains("/"+seatNumber))
                    {
                        isValid = false;
                    }
                    logd("Booking Contains Seat : "+bookingString.contains("/"+seatNumber));
                }
                System.out.println(response.toString());
                Log.d("PNR: ", "server reply: " + response.toString());
            } else {
                System.out.println("GET request not worked");
                Log.d("PNR: ", "GET request not worked");
            }

            //os.close();
            connection.disconnect();
        }
        catch (Exception e)
        {
            Log.d("PNR Status Expection","Chud gaya pnr ka API");
        }
        return isValid;
    }

    @Override
    protected void onPostExecute(Boolean isValid) {

        Log.d("PNR", "isvalid: "+isValid);
        if(isValid)
        {
            Toast.makeText(veificationFragment.getActivity() , "PNR Verification Successful", Toast.LENGTH_SHORT).show();
            FragmentManager fm = veificationFragment.getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.frameLayout, new FeedbackFormFragment());
            fragmentTransaction.commit(); // save the changes
        }
        else
        {
            final Button verifyOtpButton = (Button) veificationFragment.getView().findViewById(R.id.verify_otp_button);
           // Toast.makeText(veificationFragment.getActivity() , "PNR Verification Failed", Toast.LENGTH_SHORT).show();
            verifyOtpButton.setEnabled(true);
            String title = "PNR Verification Failed";
            String message = "You have entered a wrong/invalid PNR or not for the same Seat, Verify the same and Re-enter correct PNR to proceed.";
            Util.showInvalidDialog(veificationFragment.getActivity(),title,message);

        }

    }

}
