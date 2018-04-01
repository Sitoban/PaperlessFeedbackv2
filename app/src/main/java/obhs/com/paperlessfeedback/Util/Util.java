package obhs.com.paperlessfeedback.Util;

import java.util.List;
import java.util.Random;

import obhs.com.paperlessfeedback.Beans.Coach;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Util {

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
}
