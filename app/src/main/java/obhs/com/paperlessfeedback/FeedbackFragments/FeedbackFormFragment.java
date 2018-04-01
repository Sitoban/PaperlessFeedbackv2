package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import obhs.com.paperlessfeedback.Beans.Feedback;
import obhs.com.paperlessfeedback.FeedbackActivity;
import obhs.com.paperlessfeedback.R;

import static obhs.com.paperlessfeedback.Util.Util.getCheckedRadioButtonText;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class FeedbackFormFragment extends Fragment {

    private Feedback currentFeedback;

    public void init(View view) {

        final FeedbackActivity feedbackActivity = (FeedbackActivity)getActivity();
        currentFeedback = new Feedback(feedbackActivity.getCurrentFeedBackType(),
                                feedbackActivity.getCurrentCoach().getCoachType());

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
                currentFeedback.addScoreByRating(getCheckedRadioButtonText(radioGroup));
                currentFeedback.calculatePsi();
                Toast.makeText(feedbackActivity,
                        "PSI: " + Double.toString(currentFeedback.getPsi()) + "%", Toast.LENGTH_LONG).show();
                feedbackActivity.getCurrentCoach().addFeedback(currentFeedback);
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
}
