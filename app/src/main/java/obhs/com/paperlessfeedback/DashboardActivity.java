package obhs.com.paperlessfeedback;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import obhs.com.paperlessfeedback.DashboardFragments.DashboardFragment;
import obhs.com.paperlessfeedback.DashboardFragments.TrainSelectionFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;

public class DashboardActivity extends AppCompatActivity {

    Button secondFragment;
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
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes

    }

    private void init() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();

        // set mainDashboardView

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
        Log.d("DebugTag", "initial size: " + globalContext.getListOfTrains().size());
        AsyncTaskUtil.getDatabaseReadAsyncTask(this).execute(globalContext);
    }


}