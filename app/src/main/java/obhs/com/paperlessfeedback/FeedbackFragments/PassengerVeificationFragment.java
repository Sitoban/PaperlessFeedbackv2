package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import obhs.com.paperlessfeedback.Beans.Feedback;
import obhs.com.paperlessfeedback.Beans.Passenger;
import obhs.com.paperlessfeedback.FeedbackActivity;
import obhs.com.paperlessfeedback.Network.PNRStatusCheck;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.Util.Util;

/**
 * Created by 1018651 on 03/31/2018.
 */

public class PassengerVeificationFragment extends Fragment{
    View view;
    private static final String TAG = "Feedback";
    private static String otpNumber;
    private boolean IsMobileVerified = false;
    private boolean IsPNRVerified = false;
//    private Coach currentCoach;
//    private int currentSeatNumber;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.passenger_verification_fragment, container, false);
        return view;
    }

//    public PassengerVeificationFragment(Coach coach, int currentSeatNumber) {
//        this.currentCoach = coach;
//        this.currentSeatNumber = currentSeatNumber;
//    }

    public String generateOTP()
    {
        int max = 9000;
        int min = 1000;
        // create instance of Random class
        Random randomNum = new Random();
        int otp = min + randomNum.nextInt(max);

        return Integer.toString(otp);
    }

//    private void loadFragment(Fragment fragment) {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated fragment Activity");

        final View fragmentView = getView();

        final FeedbackActivity feedbackActivity = (FeedbackActivity) getActivity();

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Feedback");
        final Feedback.FeedbackType feedbackType = feedbackActivity.getCurrentFeedBackType();
        final Button otpButton = (Button) view.findViewById(R.id.send_otp_button);
        final TextView mobileNumberText = (TextView) view.findViewById(R.id.textViewOfMobileNumber);
        final Button resendotpButton = (Button) view.findViewById(R.id.resend_otp_button);
        final AutoCompleteTextView mobileNumberField = (AutoCompleteTextView) view.findViewById(R.id.mobile_number);
        final TextInputLayout textInputMobileNumberField = (TextInputLayout) view.findViewById(R.id.text_input_mobile_number);

        final AutoCompleteTextView pnrNumberField = (AutoCompleteTextView) view.findViewById(R.id.pnr_number);
        final TextInputLayout textInputPnrNumberField = (TextInputLayout) view.findViewById(R.id.text_input_pnr_number);

        final Button verifyOtpButton = (Button) view.findViewById(R.id.verify_otp_button);
        final AutoCompleteTextView otpNumberField = (AutoCompleteTextView) view.findViewById(R.id.otp_number);
        final TextInputLayout textInputOtpNumberField = (TextInputLayout) view.findViewById(R.id.text_input_otp_number);
        final PassengerVeificationFragment PVfragmentView = this;

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"send OTP");

               // new PNRStatusCheck(fragmentView).execute(pnrNumberField.getText().toString());
                String mobileNumberString = mobileNumberField.getText().toString();
                generateAndSendOTP(mobileNumberString);
                mobileNumberText.setText("Mobile Number : "+mobileNumberString);
                /////////Start Hiding
                mobileNumberField.setVisibility(View.GONE);
                otpButton.setVisibility(View.GONE);
                textInputMobileNumberField.setVisibility(View.GONE);



                verifyOtpButton.setVisibility(View.VISIBLE);
                resendotpButton.setVisibility(View.VISIBLE);
                otpNumberField.setVisibility(View.VISIBLE);
                textInputOtpNumberField.setVisibility(View.VISIBLE);
                mobileNumberText.setVisibility(View.VISIBLE);
                /// End Showing

            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpNumberString = otpNumberField.getText().toString();
                IsMobileVerified = otpNumberString.equals(otpNumber);
                String mobileNumber = mobileNumberField.getText().toString();

                int verificationStep = verifyOtpButton.getText() == "Verify PNR" ? 2: 1;
                if(verificationStep == 1)
                {
                    mobileNumberText.setVisibility(View.VISIBLE);
                    if(IsMobileVerified)
                    {
                        Toast.makeText(getActivity() , "Mobile Verification Successful", Toast.LENGTH_SHORT).show();

                        Log.d(TAG,"Feed Back Type : "+feedbackType);

                        if(feedbackType == Feedback.FeedbackType.PASSENGER)
                        {
                            resendotpButton.setVisibility(View.GONE);
                            pnrNumberField.setVisibility(View.VISIBLE);
                            textInputPnrNumberField.setVisibility(View.VISIBLE);

                            verifyOtpButton.setText("Verify PNR");
                            otpNumberField.setVisibility(View.GONE);
                            mobileNumberText.setVisibility(View.GONE);
                            textInputOtpNumberField.setVisibility(View.GONE);
                        }
                        else
                        {
                            feedbackActivity.setCurrentPassenger(new Passenger(mobileNumber, "-"));
                            feedbackActivity.loadFragment(new FeedbackFormFragment());
                        }

                    }
                    else
                    {
                        showInvalidMessage();
                    }
                }
                else {
                    verifyOtpButton.setEnabled(false);
                    String pnrNumber = ((AutoCompleteTextView)fragmentView.findViewById(R.id.pnr_number)).getText().toString();
                    feedbackActivity.setCurrentPassenger(new Passenger(mobileNumber, pnrNumber));

                    String seatNumber = String.valueOf(feedbackActivity.getCurrentSeatNumber());
                    //String pnrNumber =pnrNumberField.getText().toString();
                     new PNRStatusCheck(PVfragmentView).execute(pnrNumber,seatNumber);
                }

            }
        });

        resendotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumberString = mobileNumberField.getText().toString();
                onResendOTPClick(resendotpButton,mobileNumberString);
            }
        });
        //set coach number and seat number for feedback
        TextView textView = getView().findViewById(R.id.seatNumberTextView);
        textView.setText("Coach : " + feedbackActivity.getCurrentCoach().getCoachNumber() + ", Seat: " + feedbackActivity.getCurrentSeatNumber());



        Button cancelFeedbackButton = getView().findViewById(R.id.cancel_feedback);
        cancelFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "Cancel Feedback";
                String message= "Are you sure you want to cancel this feedback?";
                Util.showConfirmationDialog(getActivity(), title, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });

            }
        });
    }
    private void showInvalidMessage()
    {
        String title = "Invalid OTP";
        String message = "You have entered a wrong/invalid OTP, Verify the same and Re-enter correct OTP to proceed.";
        Util.showInvalidDialog(getActivity(),title,message);
    }

    private void onResendOTPClick(Button resendOtpButton, String mobileNumberString)
    {
        resendOtpButton.setVisibility(View.GONE);
        generateAndSendOTP(mobileNumberString);

    }
    private void generateAndSendOTP(String mobileNumberString)
    {
        otpNumber = generateOTP();
        Log.d("OTP",otpNumber);
        //TODO: Do Not Remove
        // SmsManager smsManager = SmsManager.getDefault();
        // smsManager.sendTextMessage(mobileNumberString, null, otpNumber, null, null);
        //TODO: Do Not Remove
    }
}
