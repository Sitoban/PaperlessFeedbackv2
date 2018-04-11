package obhs.com.paperlessfeedback.DashboardFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.Util.Util;

import static obhs.com.paperlessfeedback.Util.Util.logd;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class TrainSelectionFragment extends Fragment {
    View view;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.train_selection_fragment, container, false);

        Button submitButton = (Button) view.findViewById(R.id.submitTrain);
        final Spinner selectTrainSpinner = view.findViewById(R.id.selectTrainDropDown);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get selectedTrain and create trip
                GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
                globalContext.setCurrentTrainIndex(selectTrainSpinner.getSelectedItemId());
                Train selectedTrain = globalContext.getCurrentTrain();
//                Log.d("debugTag", "selectedTrain: " +  selectedTrain.getTrainName());
                Trip currentTrip = new Trip(selectedTrain);
                globalContext.setCurrentTrip(currentTrip);

                //update shared pref
                Util.updateTripStatusPref(getActivity());

                //load fragment
                loadFragment(new DashboardFragment());
            }
        });

        //disable submit button
        submitButton.setEnabled(false);
        setupTrainList(view);

        return view;

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

    private void setupTrainList(View fragmentView) {
        final GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
//        Log.d("DebugTag", "initial size: " + globalContext.getListOfTrains().size());
//        logd("called setup");
//        AsyncTaskUtil.getContextualAsyncTask(getActivity(), fragmentView).execute(globalContext);
        Spinner selectTrainSpinner = fragmentView.findViewById(R.id.selectTrainDropDown);

        List<String> trainListString = new ArrayList<String>();
        for(Train train: globalContext.getListOfTrains()) {
            trainListString.add(train.getTrainName() + " : " + train.getTrainNumber());
        }
        ArrayAdapter<String> trainSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, trainListString);
        trainSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTrainSpinner.setAdapter(trainSpinnerAdapter);

        //enable submit button
        Button submitButton = fragmentView.findViewById(R.id.submitTrain);
        submitButton.setEnabled(true);
    }

}
