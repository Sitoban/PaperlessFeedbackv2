package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import obhs.com.paperlessfeedback.FeedbackActivity;
import obhs.com.paperlessfeedback.R;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class FeedbackFormFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dashboard_fragment, container, false);
        View view = inflater.inflate(R.layout.feedback_form_fragment, container, false);

        Button takeFeedback = (Button) view.findViewById(R.id.submitButton);
        takeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity() , "Submitting Bad Boy", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}
