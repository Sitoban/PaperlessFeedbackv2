package obhs.com.paperlessfeedback.AsyncTaskHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.R;

import static obhs.com.paperlessfeedback.Util.Util.logd;
import static obhs.com.paperlessfeedback.Util.Util.setupCustomTrain;

/**
 * Created by mannis on 31-Mar-18.
 */

public class ContextualAsyncTask extends AsyncTask<GlobalContext, Void, GlobalContext> {
    private Context context;
    private View fragmentView;

    public ContextualAsyncTask(Context con, View view) {
        context = con;
        fragmentView = view;
    }
    @Override
    protected GlobalContext doInBackground(GlobalContext... globalContexts) {
        GlobalContext globalContext = globalContexts[0];

        //setting up custom train
        List<Coach> coachList = new ArrayList<Coach>();
        setupCustomTrain(coachList);
        globalContext.addTrain(new Train("Shatabdi Express",94312, 1, coachList));
        return globalContext;
    }

    @Override
    protected void onPostExecute(GlobalContext globalContext) {
//        Log.d("DebugTag", "size after setup: " + globalContext.getListOfTrains().size());
        //pupulate spinner
//        View mainDashboardView = globalContext.getMainDashboardView();
//        Spinner selectTrainSpinner = mainDashboardView.findViewById(R.id.selectTrainDropDown);
        Spinner selectTrainSpinner = fragmentView.findViewById(R.id.selectTrainDropDown);

        List<String> trainListString = new ArrayList<String>();
        for(Train train: globalContext.getListOfTrains()) {
            trainListString.add(train.getTrainName() + " : " + train.getTrainNumber());
        }
        ArrayAdapter<String> trainSpinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, trainListString);
        trainSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTrainSpinner.setAdapter(trainSpinnerAdapter);

        //enable submit button
//        Button submitButton = (Button) mainDashboardView.findViewById(R.id.submitTrain);
        Button submitButton = fragmentView.findViewById(R.id.submitTrain);
        submitButton.setEnabled(true);
    }
}
