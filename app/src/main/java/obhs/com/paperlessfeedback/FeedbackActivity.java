package obhs.com.paperlessfeedback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.FeedbackFragments.PassengerVeificationFragment;


public class FeedbackActivity extends AppCompatActivity {

    private Coach currentCoach;
    private int currentSeatNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //get intent data
        currentCoach = (Coach) getIntent().getSerializableExtra("coach");
        currentSeatNumber =  getIntent().getIntExtra("seatNumber", 0);

        PassengerVeificationFragment passengerVeificationFragment = new PassengerVeificationFragment(currentCoach, currentSeatNumber);
        loadFragment(passengerVeificationFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
