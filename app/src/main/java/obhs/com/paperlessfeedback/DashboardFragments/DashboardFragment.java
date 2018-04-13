package obhs.com.paperlessfeedback.DashboardFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import obhs.com.paperlessfeedback.AsyncTaskHandler.AsyncTaskUtil;
import obhs.com.paperlessfeedback.Beans.Feedback;
import obhs.com.paperlessfeedback.DashboardActivity;
import obhs.com.paperlessfeedback.FeedbackActivity;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.Network.CloudConnection;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;
import obhs.com.paperlessfeedback.Util.Util;

import static android.content.Context.MODE_PRIVATE;
import static obhs.com.paperlessfeedback.Util.Util.logd;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class DashboardFragment extends Fragment {

    public void setupCoachSpinner(View view) {
        Button takeFeedbackButton = view.findViewById(R.id.takeFeedbackButton);
        takeFeedbackButton.setEnabled(false);
////////////////
        GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        Spinner coachSelectionSpinner = view.findViewById(R.id.coachSelectionSpinner);
        List<String> coachListString = new ArrayList<String>();
        for(Coach coach: globalContext.getCurrentTrain().getCoachList()) {
            coachListString.add(coach.getCoachNumber());
        }
        ArrayAdapter<String> CoachSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coachListString);
        CoachSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachSelectionSpinner.setAdapter(CoachSpinnerAdapter);
///////////////
        takeFeedbackButton.setEnabled(true);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onStart() {
        super.onStart();
        updatePendingCompleted();

        Util.printCoachFeedbackCount((GlobalContext) getActivity().getApplicationContext());

        Bundle bundle = this.getArguments();
        boolean isMidWay = false;
        if (bundle != null) {
            isMidWay = bundle.getBoolean("isMidWay", false);
        }
        logd("isMidWay: " + isMidWay);
        if(isMidWay) {
            resetTripVarsForMidWay((GlobalContext) getActivity().getApplicationContext(), (Button) getView().findViewById(R.id.endTripButton));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dashboard_fragment, container, false);
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        final GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        globalContext.setLiveDashboardFragment(this);
        AsyncTaskUtil.getSetNumDbEntries(this).execute(globalContext);

        Button takeFeedback = (Button) view.findViewById(R.id.takeFeedbackButton);
        final Spinner coachSelectionSpinner = view.findViewById(R.id.coachSelectionSpinner);
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.passenger_type);
        final Fragment thisFragment = this;
        takeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating if seat is available for feedback
                long coachIndex = coachSelectionSpinner.getSelectedItemId();
                Feedback.FeedbackType passengerType = getFeedbackTypeSelection(radioGroup);
                Coach currentCoach = globalContext.getCurrentTrain().getCoachList().get((int)coachIndex);
                if(!currentCoach.isSeatAvailableForFeedback()) {
                    Toast.makeText(getActivity() , "No seat available, for feedback in this coach", Toast.LENGTH_LONG).show();
                    return;
                }

//                Toast.makeText(getActivity() , "Let's Start the Bad Boy", Toast.LENGTH_SHORT).show();

                Context context = view.getContext();
                Intent intent = new Intent(context, FeedbackActivity.class);

                //adding coach info to new acitivity
//                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

//                intent.putExtra("coach", currentCoach);
                globalContext.setCurrentLiveCoach(currentCoach);
//                Log.d("debugTag", "this Fragment indent push: " + (Serializable)thisFragment);
//                intent.putExtra("dashboard", (Serializable)thisFragment);
                intent.putExtra("seatNumber", currentCoach.getRandomSeat());
                //edit: pass appropriate type -- TT or passenger
                Log.d("FeedbackType : ",passengerType.toString());
                intent.putExtra("feedbackType", passengerType);
                context.startActivity(intent);
            }
        });

        setupCoachSpinner(view);

        final Button endTripButton = view.findViewById(R.id.endTripButton);
        endTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalContext.getCurrentTrip().getTripStatus() == Trip.TripStatus.GOING) {
                    //edit: test that if number of pending sync > 0, then end trip is disabled
//                    boolean enable = (globalContext.getNumLocalDbFeedbacks() == 0)?true:false;
//                    endTripButton.setEnabled(enable);
                    //reset preferences for coach num feedbacks

                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int index = 0;
                            for(Coach coach : globalContext.getCurrentTrain().getCoachList()) {
                                Util.getPrefEditor(getActivity()).putString("Coach" + index, "0:0").apply();
                                ++index;
                            }


                            Util.updateTripStatusPref(getActivity(), Trip.getTripStatusIntVal(Trip.TripStatus.ARRIVING));

                            globalContext.getCurrentTrain().resetCoachNumFeedbacks();
                            resetTripVarsForMidWay(globalContext, endTripButton);

                        }};
                    String title = "Mid Trip Confirmation";
                    String message = "Are you sure you want to end Onwards Journey?";
                    Util.showConfirmationDialog(getActivity(),title,message,listener);

                }
                else if(globalContext.getCurrentTrip().getTripStatus() == Trip.TripStatus.ARRIVING) {
                    showConfirmationMessageAndEndTrip(globalContext);
//                    Util.removeAllPrefs(getActivity());
//                    ((DashboardActivity)getActivity()).loadFragment(globalContext.getLiveTrainSelectionFragment());
                }
               // Toast.makeText(getActivity(), "current trip status: " + globalContext.getCurrentTrip().getTripStatus(),
               //         Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton syncButton = view.findViewById(R.id.syncButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackObj nullFeedbackObj = null;
                new CloudConnection(globalContext).execute(nullFeedbackObj);
            }
        });

        return view;
    }

    public void resetTripVarsForMidWay(GlobalContext globalContext, Button endTripButton) {
        globalContext.getCurrentTrip().setNextTripState();
        endTripButton.setText("End Trip");
        updatePendingCompleted();
    }

    public void setNumEntriesLocal(int n) {
//        Log.d("debugTag", "setting numEntries: " + n);
        TextView numEntriesLocalTextView = getView().findViewById(R.id.numEntriesLocal);
        numEntriesLocalTextView.setText("Sync Pending: " + n);
    }

    public void setEndTripButtonEnabled(Boolean enable) {
        Button endTripButton = getView().findViewById(R.id.endTripButton);
        endTripButton.setEnabled(enable);
    }

    public void updatePendingCompleted() {

        final GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        TextView completedFeedbackTextView = getView().findViewById(R.id.completedFeedbackTextView);
        TextView pendingFeedbackTextView = getView().findViewById(R.id.pendingFeedbackTextView);

        completedFeedbackTextView.setText(Integer.toString(globalContext.getCurrentTrip().getNumCompletedFeedbacks()));
        pendingFeedbackTextView.setText(Integer.toString(globalContext.getCurrentTrip().getNumPendingFeedbacks()));
    }

    public Feedback.FeedbackType getFeedbackTypeSelection(RadioGroup radioGroup)
    {
        return radioGroup.getCheckedRadioButtonId() == R.id.tt_button ? Feedback.FeedbackType.TTE: Feedback.FeedbackType.PASSENGER;
    }

    private void showConfirmationMessageAndEndTrip(final GlobalContext globalContext) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Util.removeAllPrefs(getActivity());
                ((DashboardActivity)getActivity()).loadFragment(globalContext.getLiveTrainSelectionFragment());
            }
        };
        String title = "End Trip Confirmation";
        String message = getEndTripConfirmationMessage(globalContext);
        Util.showConfirmationDialog(getActivity(),title,message,listener);
    }


    private String getEndTripConfirmationMessage(GlobalContext globalContext)
    {

        String message = "Do you really want to end the trip?";
        if(globalContext.getNumLocalDbFeedbacks() > 0)
        {
            message = "There are some feedback yet to be synced, "+message;
        }
        return message;
    }


}
