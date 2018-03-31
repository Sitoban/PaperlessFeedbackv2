package obhs.com.paperlessfeedback.Beans;

import java.util.List;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Train {
    public int getTrainNumber() {
        return trainNumber;
    }

    private int trainNumber;
    private int rakeNumber;

    public String getTrainName() {
        return trainName;
    }

    private String trainName;

    public List<Coach> getCoachList() {
        return coachList;
    }

    //    private int numberOfCoaches;
    private List<Coach> coachList;

//    public Train(int tn, int rn, int noc, List<Coach> cl) {
    public Train(String trName, int tn, int rn, List<Coach> cl) {
        trainName = trName;
        trainNumber = tn;
        rakeNumber = rn;
//        numberOfCoaches = noc;
        coachList = cl;
    }

//    //edit: delete
//    public Train() {
//
//    }

}
