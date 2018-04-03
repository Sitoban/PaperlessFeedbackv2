package obhs.com.paperlessfeedback.Network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

/**
 * Created by 1018651 on 04/03/2018.
 */

public class CloudConnection extends AsyncTask<FeedbackObj, Integer, Long> {
    @Override
    protected Long doInBackground(FeedbackObj... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        FeedbackObj feedback = params[0];
        //String POST_PARAMS = "PSI=120&CoachNumber=S1&SeatNumber=40&MobileNumber=8008713000&TrainNumber=1234&Date=2018-04-01&TripID=201804011238&TrainStartDate=2018-04-01&PNRNumber=1018651";
        try {
             /*
             URL url = new URL("https://paperlessfeedbackautomation.appspot.com/Feedback");
             */
            URL url = new URL("http://paperlessfeedbackautomation.appspot.com/Feedback");
            connection = (HttpURLConnection) url.openConnection();
            //connection.connect();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(feedback.getQueryString().getBytes());
            os.flush();
            os.close();



            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            String finalJson = buffer.toString();
            Log.d("Response msg : ",finalJson);
            System.out.println(finalJson);

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
        return Long.MIN_VALUE;
    }
}
