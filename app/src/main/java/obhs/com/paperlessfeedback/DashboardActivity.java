package obhs.com.paperlessfeedback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import obhs.com.paperlessfeedback.DashboardFragments.TrainSelectionFragment;
import android.util.Log;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;

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
        loadFragment(new TrainSelectionFragment());

        init();
        //setup trains list
        setupTrainList();
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void init() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();

        // set mainDashboardView
        globalContext.setDb(AppDatabase.getAppDatabase(this));
        View view = findViewById(R.id.frameLayout);
        globalContext.setMainDashboardView(view);

        //set submit button action
//        Button submitButton = (Button) view.findViewById(R.id.submitTrain);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(DashboardActivity.this, "Pressed submit",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        //disable submit button
//        submitButton.setEnabled(false);
//
    }

    private void setupTrainList() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
//        Log.d("DebugTag", "initial size: " + globalContext.getListOfTrains().size());
        AsyncTaskUtil.getContextualAsyncTask(this).execute(globalContext);
    }


}