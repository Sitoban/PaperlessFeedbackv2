package obhs.com.paperlessfeedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

        init();
        //setup trains list
        setupTrainList();
    }

    private void init() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();

        // set mainDashboardView
        View view = findViewById(R.id.mainDashboardView);
        globalContext.setMainDashboardView(view);

        //set submit button action
        Button submitButton = (Button) view.findViewById(R.id.submitTrain);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "Pressed submit",
                        Toast.LENGTH_LONG).show();
            }
        });

        //disable submit button
        submitButton.setEnabled(false);

//        List<Train> trainsList = globalContext.getListOfTrains();
//        globalContext.getListOfTrains().add(new Train());
    }

    private void setupTrainList() {
        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
        Log.d("DebugTag", "initial size: " + globalContext.getListOfTrains().size());
        AsyncTaskUtil.getDatabaseReadAsyncTask(this).execute(globalContext);
    }
}
