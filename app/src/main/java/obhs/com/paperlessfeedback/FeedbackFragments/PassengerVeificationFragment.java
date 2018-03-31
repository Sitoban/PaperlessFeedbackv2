package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import obhs.com.paperlessfeedback.DashboardFragments.DashboardFragment;
import obhs.com.paperlessfeedback.R;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class PassengerVeificationFragment extends Fragment{
    View view;
    private static final String TAG = "Feedback";
    private static String otpNumber;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.passenger_verification_fragment, container, false);
        return view;
    }
    public String generateOTP()
    {
        int max = 9999;
        int min = 1000;
        // create instance of Random class
        Random randomNum = new Random();
        int otp = min + randomNum.nextInt(max);

        return Integer.toString(otp);
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
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated fragment Activity");
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Feedback");
        final Button otpButton = (Button) view.findViewById(R.id.send_otp_button);
        final AutoCompleteTextView mobileNumberField = (AutoCompleteTextView) view.findViewById(R.id.mobile_number);
        final TextInputLayout textInputMobileNumberField = (TextInputLayout) view.findViewById(R.id.text_input_mobile_number);

        final AutoCompleteTextView pnrNumberField = (AutoCompleteTextView) view.findViewById(R.id.pnr_number);
        final TextInputLayout textInputPnrNumberField = (TextInputLayout) view.findViewById(R.id.text_input_pnr_number);

        final Button verifyOtpButton = (Button) view.findViewById(R.id.verify_otp_button);
        final AutoCompleteTextView otpNumberField = (AutoCompleteTextView) view.findViewById(R.id.otp_number);
        final TextInputLayout textInputOtpNumberField = (TextInputLayout) view.findViewById(R.id.text_input_otp_number);

        final TextView result = (TextView) view.findViewById(R.id.result);

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"send OTP");

                String mobileNumberString = mobileNumberField.getText().toString();
                otpNumber = generateOTP();
                // SmsManager smsManager = SmsManager.getDefault();
                // smsManager.sendTextMessage(mobileNumberString, null, otpNumber, null, null);

                /////////Start Hiding
                mobileNumberField.setVisibility(View.GONE);
                otpButton.setVisibility(View.GONE);
                textInputMobileNumberField.setVisibility(View.GONE);
                pnrNumberField.setVisibility(View.GONE);
                textInputPnrNumberField.setVisibility(View.GONE);
                ////// End Hiding

                /// Start Showing
                verifyOtpButton.setVisibility(View.VISIBLE);
                otpNumberField.setVisibility(View.VISIBLE);
                textInputOtpNumberField.setVisibility(View.VISIBLE);
                /// End Showing

                //attemptLogin();
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpNumberString = otpNumberField.getText().toString();

                //if(otpNumberString.equals(otpNumber))
                if(true)
                {
                    Toast.makeText(getActivity() , "Passenger Verification Successful", Toast.LENGTH_SHORT).show();

                    loadFragment(new FeedbackFormFragment());

                    //result.setText("Success");
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Alert Dialog");

                    // Setting Dialog Message
                    alertDialog.setMessage("Invalid OTP");

                    // Setting Icon to Dialog

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed

                            //Toast.makeText(getActivity() , "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }

            }
        });
    }
}