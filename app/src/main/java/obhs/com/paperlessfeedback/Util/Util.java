package obhs.com.paperlessfeedback.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Util {

    final static  String PREF_FILE = "tripConfigPrefs";

    private static void swapArrayVal(int ar[], int i, int j) {
        int val = ar[i];
        ar[i] = ar[j];
        ar[j] = val;
    }

    // need to be moved to Trip
    public static void setupCustomTrain(List<Coach> coachList) {
        //2 AC + 2 NON AC coach
//        coachList.add(new Coach("A1", 5, Coach.CoachType.AC));
//        coachList.add(new Coach("A2", 5, Coach.CoachType.AC));
//        coachList.add(new Coach("S1", 10, Coach.CoachType.NON_AC));
//        coachList.add(new Coach("S2", 10, Coach.CoachType.NON_AC));

        coachList.add(new Coach("C1", 78, Coach.CoachType.AC));
        coachList.add(new Coach("D1", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D2", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D3", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D4", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D5", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D6", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D7", 108, Coach.CoachType.NON_AC));
        coachList.add(new Coach("D8", 108, Coach.CoachType.NON_AC));
    }


    public static void randomizeArray( int ar[], int n )
    {
        Random random = new Random();
        for (int i = n; i > 1; i--)
        {
            int j = random.nextInt(i);
            swapArrayVal(ar, i-1, j);
        }
    }

    public static String getCheckedRadioButtonText(RadioGroup radioGroup) {
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        String buttonText = radioButton.getText().toString();
        return buttonText;
    }

    public static String getStringFromDate(Date date) {
        return (new SimpleDateFormat("dd-MM-yyyy").format(date));
    }

    public static Date getDateFromString(String dateString) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            loge("date parsing error");
            e.printStackTrace();
        }
        return date;
    }

    public static void updateTripStatusPref(Activity activity, int tripStatus) {
//        GlobalContext globalContext = (GlobalContext) activity.getApplicationContext();

        SharedPreferences.Editor editor = activity.getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit();
        editor.putInt("tripProgress", tripStatus).apply();
        Log.d("debugTag", "tripProgress: " + getTripStatusPref(activity));
    }

    public static int getTripStatusPref(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return prefs.getInt("tripProgress", -1);
    }

    public static void removeAllPrefs(Activity activity) {
        activity.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().clear().commit();
        Log.d("debugTag", "tripProgress2: " + getTripStatusPref(activity));
    }

    public static void removePrefByName(Activity activity, String prefName) {
        activity.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().remove(prefName).commit();
    }

    public static SharedPreferences getSharedPrefs(Activity activity) {
        return activity.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getPrefEditor(Activity activity) {
        return getSharedPrefs(activity).edit();
    }

    public static void logd(String msg) {
        Log.d("debugTag", msg);
    }

    public static void loge(String msg) {
        Log.e("debugTag", msg);
    }

    public static void printCoachFeedbackCount(GlobalContext globalContext) {
        Train train = globalContext.getCurrentTrain();
        logd("-----------------");
        int i = 0;
        for(Coach coach: train.getCoachList()) {
            logd("coachId: " + train.getCoachList().indexOf(coach) + " : " + i  + ", name: " + coach.getCoachName() + ", numPas: " + coach.getNumPasFeedback() + ", numtte: " + coach.getNumTteFeedback());
            i++;
        }
        logd("-----------------");
    }
    public static void showConfirmationDialog(Context context, String title, String message,
                                         DialogInterface.OnClickListener positivelistener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("Yes",positivelistener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void showInvalidDialog(Context context, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    public static String getMasterKeyForEverthing(MasterKeyType type)
    {
        String masterKey;
        Calendar rightNow = Calendar.getInstance();
        int date = rightNow.get(Calendar.DATE);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        masterKey = date+""+hour;
        int formatFactor = type == MasterKeyType.OTP ? 4 : 10;
        return String.format("%1$" + formatFactor + "s", masterKey).replace(' ', '0');
    }

    public enum MasterKeyType
    {
        PNR,
        OTP
    }
}
