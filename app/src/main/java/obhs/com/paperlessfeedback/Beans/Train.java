
package obhs.com.paperlessfeedback.Beans;

import java.util.List;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Train {
    private int trainNumber;
    private int rakeNumber;
    private String trainName;
    //    private int numberOfCoaches;
    private List<Coach> coachList;
    private int numRequiredFeedbacks;
    private final static int NUM_FEEDBACK_PER_COACH_PER_TYPE = 2;

    public Coach getCoachFromCoachName(String coachName) {
        Coach result = null;
        for(Coach coach: coachList) {
            if(coach.getCoachName() == coachName){
                result = coach;
                break;
            }
        }
        return result;
    }

//    public Train(int tn, int rn, int noc, List<Coach> cl) {
    public Train(String trName, int tn, int rn, List<Coach> cl) {
        trainName = trName;
        trainNumber = tn;
        rakeNumber = rn;
//        numberOfCoaches = noc;
        coachList = cl;
        //call after setting coachList

        getSetNumRequiredFeedbacks();
    }

    private void getSetNumRequiredFeedbacks() {
        //edit: make changes for accomodating tte feedback
        numRequiredFeedbacks = (coachList.size()) * NUM_FEEDBACK_PER_COACH_PER_TYPE * 2;
    }

    public String getTrainName() {
        return trainName;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public List<Coach> getCoachList() {
        return coachList;
    }

    public int getNumRequiredFeedbacks() {
        return numRequiredFeedbacks;
    }

    public void resetCoachNumFeedbacks() {
        for(Coach coach: getCoachList()) {
            coach.resetNumFeedbacks();
        }
    }

}
