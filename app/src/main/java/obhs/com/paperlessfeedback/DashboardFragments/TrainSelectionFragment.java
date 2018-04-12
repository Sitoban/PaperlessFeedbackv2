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
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.Util.Util;

import static obhs.com.paperlessfeedback.Util.Util.getSharedPrefs;
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
    public void onStart() {
        super.onStart();
        int tripStatus = Util.getTripStatusPref(getActivity());
        if(tripStatus == -1) {
            //do nothing
        }
        else {
            GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
            long trainIndex = getSharedPrefs(getActivity()).getLong("trainIndex", -1);
            if(trainIndex  != -1) {
                setGlobalVars(trainIndex, true);
                int index = 0;
                for(Coach coach : globalContext.getCurrentTrain().getCoachList()) {
                    String coachFBstr = Util.getSharedPrefs(getActivity()).getString("Coach" + index , "null");
                    if(coachFBstr == "null") {
                        logd("unexpected coach feedback info");
                    }
                    else {
                        String [] splitFB = coachFBstr.split(":");
                        coach.setNumPasFeedback(Integer.parseInt(splitFB[0]));
                        coach.setNumTteFeedback(Integer.parseInt(splitFB[1]));
                    }
                    ++index;
                }
                //load fragment
                Fragment dashboardFragment = new DashboardFragment();
                Bundle bundle = new Bundle();
                if(tripStatus == 0) {
                    bundle.putBoolean("isMidWay", false);
                }
                else if(tripStatus == 1) {
                    bundle.putBoolean("isMidWay", true);
                }
                dashboardFragment.setArguments(bundle);
                loadFragment(dashboardFragment);
            }
        }
    }

    public void setGlobalVars(long trainIndex, boolean restore) {
        GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        globalContext.setCurrentTrainIndex(trainIndex);
        Train selectedTrain = globalContext.getCurrentTrain();
        Trip currentTrip = null;
        if(!restore) {
            currentTrip = new Trip(selectedTrain);
        }
        else {
            int tripStatus = Util.getTripStatusPref(getActivity());
            String tripDateString = Util.getSharedPrefs(getActivity()).getString("tripDate", "11-11-1111");
            logd("tripDateString: " + tripDateString);
            logd("tripDate: " + Util.getDateFromString(tripDateString));
            long tripId = Util.getSharedPrefs(getActivity()).getLong("tripId", -1);
            logd("tripId: " + tripId);
            currentTrip = new Trip(selectedTrain, Trip.getTripStatusFromIntVal(tripStatus), Util.getDateFromString(tripDateString), tripId);
        }
        globalContext.setCurrentTrip(currentTrip);

    }

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
                long trainIndex = selectTrainSpinner.getSelectedItemId();
                setGlobalVars(trainIndex, false);
//                globalContext.setCurrentTrainIndex(trainIndex);
//                Train selectedTrain = globalContext.getCurrentTrain();
//                Log.d("debugTag", "selectedTrain: " +  selectedTrain.getTrainName());
//                Trip currentTrip = new Trip(selectedTrain);
//                globalContext.setCurrentTrip(currentTrip);

                //update shared pref
                Util.updateTripStatusPref(getActivity(), Trip.getTripStatusIntVal(Trip.TripStatus.GOING));
                Util.getPrefEditor(getActivity()).putLong("trainIndex", trainIndex).apply();
                logd("saving date: " + Util.getStringFromDate(globalContext.getCurrentTrip().getStartTime()));
                Util.getPrefEditor(getActivity()).putString("tripDate", Util.getStringFromDate(globalContext.getCurrentTrip().getStartTime())).apply();
                Util.getPrefEditor(getActivity()).putLong("tripId", globalContext.getCurrentTrip().getTripId()).apply();
                int index = 0;
                for(Coach coach : globalContext.getCurrentTrain().getCoachList()) {
                    Util.getPrefEditor(getActivity()).putString("Coach" + index, "0:0").apply();
                    ++index;
                }

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
