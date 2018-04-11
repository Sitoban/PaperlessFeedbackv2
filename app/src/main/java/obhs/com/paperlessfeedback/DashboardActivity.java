package obhs.com.paperlessfeedback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.DashboardFragments.TrainSelectionFragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;

import static obhs.com.paperlessfeedback.Util.Util.logd;
import static obhs.com.paperlessfeedback.Util.Util.setupCustomTrain;

public class DashboardActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //disabling back button
    }

    Button secondFragment;

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("debugTag", "resuming activity");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
        //setup trains list
        //edit: setting up custom train
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
        List<Coach> coachList = new ArrayList<Coach>();
        setupCustomTrain(coachList);
        globalContext.addTrain(new Train("Shatabdi Express",94312, 1, coachList));
    }


    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void init() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();

        //set first fragment
        TrainSelectionFragment trainSelectionFragment = new TrainSelectionFragment();
        loadFragment(trainSelectionFragment);
        globalContext.setLiveTrainSelectionFragment(trainSelectionFragment);

        globalContext.setDb(AppDatabase.getAppDatabase(this));
        View view = findViewById(R.id.frameLayout);
    }

}