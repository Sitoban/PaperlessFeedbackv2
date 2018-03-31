package obhs.com.paperlessfeedback.Util;

import java.util.List;

import obhs.com.paperlessfeedback.Beans.Coach;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Util {
    public static void setupCustomTrain(List<Coach> coachList) {
        //2 AC + 2 NON AC coach
        coachList.add(new Coach("A1", 70, Coach.CoachType.AC));
        coachList.add(new Coach("A2", 70, Coach.CoachType.AC));
        coachList.add(new Coach("S1", 60, Coach.CoachType.NON_AC));
        coachList.add(new Coach("S2", 60, Coach.CoachType.NON_AC));
    }
}
