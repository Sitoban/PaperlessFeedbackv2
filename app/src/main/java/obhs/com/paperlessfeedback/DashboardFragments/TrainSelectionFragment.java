package obhs.com.paperlessfeedback.DashboardFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.R;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class TrainSelectionFragment extends Fragment {
    View view;
    Button firstButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


       // return inflater.inflate(R.layout.train_selection_fragment, container, false);

        view = inflater.inflate(R.layout.train_selection_fragment, container, false);

        /////mannipec///
        Button submitButton = (Button) view.findViewById(R.id.submitTrain);
        final Spinner selectTrainSpinner = view.findViewById(R.id.selectTrainDropDown);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get selectedTrain and create trip
                GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
                globalContext.setCurrentTrainIndex(selectTrainSpinner.getSelectedItemId());
                Train selectedTrain = globalContext.getCurrentTrain();
                Log.d("debugTag", "selectedTrain: " +  selectedTrain.getTrainName());
                Trip currentTrip = new Trip(selectedTrain);
                globalContext.setCurrentTrip(currentTrip);

                //load fragment
                loadFragment(new DashboardFragment());
            }
        });

        //disable submit button
        submitButton.setEnabled(false);
        ////////


//// get the reference of Button
//        firstButton = (Button) view.findViewById(R.id.firstButton);
//// perform setOnClickListener on first Button
//        firstButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// display a message by using a Toast
//               // Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
//                loadFragment(new DashboardFragment());
//            }
//        });
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


}
