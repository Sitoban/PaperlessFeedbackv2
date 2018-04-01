package obhs.com.paperlessfeedback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Feedback;
import obhs.com.paperlessfeedback.FeedbackFragments.PassengerVeificationFragment;


public class FeedbackActivity extends AppCompatActivity {

    private Coach currentCoach;
    private int currentSeatNumber;
    private Feedback.FeedbackType currentFeedBackType;

    @Override
    public void onBackPressed() {
        //disabling back button
    }

    public void init() {
        //get intent data
        currentCoach = (Coach) getIntent().getSerializableExtra("coach");
        currentSeatNumber =  getIntent().getIntExtra("seatNumber", 0);
        if(currentSeatNumber <= 0)
            Log.e("debugTag","seat number invalid");
        currentFeedBackType =  (Feedback.FeedbackType)getIntent().getSerializableExtra("feedbackType");
//        Toast.makeText(this , "currentFBType: " + currentFeedBackType, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();

        PassengerVeificationFragment passengerVeificationFragment = new PassengerVeificationFragment();
        loadFragment(passengerVeificationFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public Feedback.FeedbackType getCurrentFeedBackType() {
        return currentFeedBackType;
    }

    public Coach getCurrentCoach() {
        return currentCoach;
    }

    public int getCurrentSeatNumber() {
        return currentSeatNumber;
    }
}
