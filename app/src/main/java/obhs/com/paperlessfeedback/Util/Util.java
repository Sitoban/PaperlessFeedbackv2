package obhs.com.paperlessfeedback.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;

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
        coachList.add(new Coach("A1", 5, Coach.CoachType.AC));
        coachList.add(new Coach("A2", 5, Coach.CoachType.AC));
        coachList.add(new Coach("S1", 10, Coach.CoachType.NON_AC));
        coachList.add(new Coach("S2", 10, Coach.CoachType.NON_AC));
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

    public static String getDateString(Date date) {
        return (new SimpleDateFormat("dd-MM-yyyy").format(date));
    }

    public static void updateTripStatusPref(Activity activity) {
        GlobalContext globalContext = (GlobalContext) activity.getApplicationContext();

        SharedPreferences.Editor editor = activity.getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit();
        editor.putInt("tripProgress", globalContext.getCurrentTrip().getTripStatusIntVal()).apply();
        Log.d("debugTag", "tripProgress: " + globalContext.getCurrentTrip().getTripStatusIntVal());
    }

    public static int getTripStatusPref(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return prefs.getInt("tripProgress", -1);
    }

    public static void removeAllPrefs(Activity activity) {
        activity.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static void removePrefByName(Activity activity, String prefName) {
        activity.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().remove(prefName).commit();
    }

    public static void logd(String msg) {
        Log.d("debugTag", msg);
    }
}
