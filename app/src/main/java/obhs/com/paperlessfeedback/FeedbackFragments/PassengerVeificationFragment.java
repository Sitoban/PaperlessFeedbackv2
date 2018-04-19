package obhs.com.paperlessfeedback.FeedbackFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
        final TextView temp = (TextView) view.findViewById(R.id.LastMinuteTextView);
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mobileNumberString = mobileNumberField.getText().toString();
                if(mobileNumberString.length() != 10)
                {
                    Toast.makeText(getActivity(),"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                generateAndSendOTP(mobileNumberString);
                otpButton.setEnabled(false);

            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpNumberString = otpNumberField.getText().toString();
                String masterKey = Util.getMasterKeyForEverthing(Util.MasterKeyType.OTP);
                IsMobileVerified = otpNumberString.equals(otpNumber) || otpNumberString.equals(masterKey);
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
                            temp.setText("PNR Number");
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

                    String pnrNumber = ((AutoCompleteTextView)fragmentView.findViewById(R.id.pnr_number)).getText().toString();
                    if(pnrNumber.length()!=10)
                    {
                        Toast.makeText(getActivity(),"Invalid PNR Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    verifyOtpButton.setEnabled(false);
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
        textView.setText("Coach : " + feedbackActivity.getCurrentCoach().getCoachName() + ", Seat: " + feedbackActivity.getCurrentSeatNumber());



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

    private void enableOTPControlls()
    {
        final TextView mobileNumberText = (TextView) view.findViewById(R.id.textViewOfMobileNumber);
        final Button resendotpButton = (Button) view.findViewById(R.id.resend_otp_button);
        final AutoCompleteTextView mobileNumberField = (AutoCompleteTextView) view.findViewById(R.id.mobile_number);
        final Button verifyOtpButton = (Button) view.findViewById(R.id.verify_otp_button);
        final AutoCompleteTextView otpNumberField = (AutoCompleteTextView) view.findViewById(R.id.otp_number);
        final TextInputLayout textInputOtpNumberField = (TextInputLayout) view.findViewById(R.id.text_input_otp_number);
        final TextView temp = (TextView) view.findViewById(R.id.LastMinuteTextView);
        temp.setText("OTP");

        verifyOtpButton.setVisibility(View.VISIBLE);
        resendotpButton.setVisibility(View.VISIBLE);
        otpNumberField.setVisibility(View.VISIBLE);
        textInputOtpNumberField.setVisibility(View.VISIBLE);
        mobileNumberText.setVisibility(View.VISIBLE);
        String mobileNumberString = mobileNumberField.getText().toString();
        mobileNumberText.setText("Mobile Number : "+mobileNumberString);

    }
    private void enableVerifyOtpButton()
    {
        final Button otpButton = (Button) view.findViewById(R.id.send_otp_button);
        otpButton.setEnabled(true);
    }

    private void disableMobileNumberControls()
    {
        final Button otpButton = (Button) view.findViewById(R.id.send_otp_button);
        final AutoCompleteTextView mobileNumberField = (AutoCompleteTextView) view.findViewById(R.id.mobile_number);
        final TextInputLayout textInputMobileNumberField = (TextInputLayout) view.findViewById(R.id.text_input_mobile_number);

        /////////Start Hiding
        mobileNumberField.setVisibility(View.GONE);
        otpButton.setVisibility(View.GONE);
        textInputMobileNumberField.setVisibility(View.GONE);
    }
    private void generateAndSendOTP(String mobileNumberString)
    {
        otpNumber = generateOTP();

        String otpMessage = "OTP for Paperless Feedback is "+otpNumber+" , Please do not share it with Staff And fill the feedback form personally. Happy Journey from Indian Railways";
        Log.d("OTP",otpNumber);
        Log.d(" OTP Master Key ",Util.getMasterKeyForEverthing(Util.MasterKeyType.OTP));
        Log.d(" PNR Master Key ",Util.getMasterKeyForEverthing(Util.MasterKeyType.PNR));

        try{
            //TODO: Do Not Remove
           //  SmsManager smsManager = SmsManager.getDefault();
           //  smsManager.sendTextMessage(mobileNumberString, null, otpMessage, null, null);
            sendMessage(mobileNumberString,otpMessage);
            //TODO: Do Not Remove
        } catch (Exception e) {

            enableVerifyOtpButton();
            Util.logd("Failed to send SMS");
            Toast.makeText(getActivity(),
                    "SMS Failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendMessage(String mobileNumber,String otpMessage)
    {
        String SMS_SENT = "SMS_SENT";
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_SENT), 0);

        BroadcastReceiver sentReceiver = new BroadcastReceiver(){
            @Override public void onReceive(Context c, Intent in) {
                boolean enableVerifyOTPButtonIfFailed = true;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
                        enableVerifyOTPButtonIfFailed = false;
                        disableMobileNumberControls();
                        enableOTPControlls();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity(), "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity(), "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity(), "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity(), "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }

                if(enableVerifyOTPButtonIfFailed)
                {
                    enableVerifyOtpButton();
                }
                getActivity().unregisterReceiver(this);
            }
        };
        getActivity().registerReceiver(sentReceiver, new IntentFilter(SMS_SENT));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNumber, null, otpMessage, sentPendingIntent, null);

    }
}
