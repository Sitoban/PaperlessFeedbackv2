package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Feedback;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.FeedbackActivity;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;
import obhs.com.paperlessfeedback.Util.CameraHelper;
import obhs.com.paperlessfeedback.Util.Util;

import static obhs.com.paperlessfeedback.Util.Util.getCheckedRadioButtonText;
import static obhs.com.paperlessfeedback.Util.Util.logd;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class FeedbackFormFragment extends Fragment {

    public void init(View view) {

        final GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        final FeedbackActivity feedbackActivity = (FeedbackActivity)getActivity();
        logd("current type:" + feedbackActivity.getCurrentFeedBackType());
        feedbackActivity.setCurrentFeedback(new Feedback(feedbackActivity.getCurrentFeedBackType(),
                                        feedbackActivity.getCurrentCoach().getCoachType()));
        final Feedback currentFeedback = feedbackActivity.getCurrentFeedback();

        final Button nextQuestionButton = (Button) view.findViewById(R.id.nextQuestionButton);
        final Button submitFeedbackButton = (Button) view.findViewById(R.id.submitFeedbackButton);
        nextQuestionButton.setEnabled(false);
        submitFeedbackButton.setEnabled(false);
        final TextView feedbackQuestionTextView = view.findViewById(R.id.feedbackQuestionTextView);
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioRatings);

        String firstQuestion = currentFeedback.getNextQuestion();
        if(firstQuestion == null)
            Log.e("debugTag", "feedback question is null");
        feedbackQuestionTextView.setText(firstQuestion);
        nextQuestionButton.setEnabled(true);

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit: remove code duplication from this and below
                String nextQuestion = currentFeedback.getNextQuestion();
                if(nextQuestion == null)
                    Log.e("debugTag", "feed back question is null");
                if(currentFeedback.isAtLastQuestion()) {
                    nextQuestionButton.setEnabled(false);
                    submitFeedbackButton.setEnabled(true);
                }
                feedbackQuestionTextView.setText(nextQuestion);

                currentFeedback.addScoreByRating(getCheckedRadioButtonText(radioGroup));
            }
        });

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedbackButton.setEnabled(false);
                currentFeedback.addScoreByRating(getCheckedRadioButtonText(radioGroup));
                currentFeedback.calculatePsi();
                Toast.makeText(feedbackActivity,
                        "PSI: " + Double.toString(currentFeedback.getPsi()) + "%", Toast.LENGTH_LONG).show();
                feedbackActivity.getCurrentCoach().addFeedback(currentFeedback);

                //update coach pref
                logd("coachIndex: " + globalContext.getCurrentTrain().getCoachList().indexOf(feedbackActivity.getCurrentCoach()));
                logd("string: " + String.valueOf(feedbackActivity.getCurrentCoach().getNumPasFeedback()) + ":" + String.valueOf(feedbackActivity.getCurrentCoach().getNumTteFeedback()));
                Util.getPrefEditor(getActivity()).putString("Coach" + globalContext.getCurrentTrain().getCoachList().indexOf(feedbackActivity.getCurrentCoach()),
                        String.valueOf(feedbackActivity.getCurrentCoach().getNumPasFeedback()) + ":" + String.valueOf(feedbackActivity.getCurrentCoach().getNumTteFeedback()) ).apply();

                addFeedbackToDatabase();
                getActivity().finish();
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dashboard_fragment, container, false);
        View view = inflater.inflate(R.layout.feedback_form_fragment, container, false);

        init(view);

        return view;
    }

    public void addFeedbackToDatabase() {
        GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
        final FeedbackActivity feedbackActivity = (FeedbackActivity)getActivity();

        FeedbackObj feedbackObj = new FeedbackObj();
        feedbackObj.setTripId(String.valueOf(globalContext.getCurrentTrip().getTripId()));
        feedbackObj.setTrainNumber(String.valueOf(globalContext.getCurrentTrip().getTrain().getTrainNumber()));
        feedbackObj.setTripStartDate(Util.getStringFromDate(globalContext.getCurrentTrip().getStartTime()));
        feedbackObj.setPnr(feedbackActivity.getCurrentPassenger().getPnr());
        feedbackObj.setMobileNumber(feedbackActivity.getCurrentPassenger().getMobileNumber());
        feedbackObj.setCoachNumber(feedbackActivity.getCurrentCoach().getCoachName());
        feedbackObj.setSeatNumber(String.valueOf(feedbackActivity.getCurrentSeatNumber()));
        feedbackObj.setPsi(String.valueOf(feedbackActivity.getCurrentFeedback().getPsi()));
        feedbackObj.setTripDirection((globalContext.getCurrentTrip().getTripStatus() == Trip.TripStatus.GOING)?0:1);

//        feedbackObj.setTrainNumber(String.valueOf(globalContext.getCurrentTrip().getTrain().getTrainNumber()));

        new CameraHelper(feedbackObj,globalContext).ShootFace(getActivity());
        //new CloudConnection().execute(feedbackObj);

        //write feebdackObj to local db
//        AsyncTaskUtil.getFeedbackObjWriteAsyncTask(feedbackObj).execute(globalContext.getDb());
        //write to local db and server
        //new CloudConnection(feedbackObj, globalContext.getLiveDashboardFragment()).execute(globalContext);


//        AsyncTaskUtil.getDatabaseReadAsyncTask().execute(globalContext.getDb());
    }
}
