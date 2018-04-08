package obhs.com.paperlessfeedback.Beans;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import obhs.com.paperlessfeedback.Util.Util;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Coach implements Serializable {
    public enum CoachType {
        AC,
        NON_AC;
    }

    private String coachNumber;
    private int numberOfSeats;
    private CoachType coachType;
//    private List<Feedback> tteFeedback= new ArrayList<Feedback>();
//    private List<Feedback> pasFeedback= new ArrayList<Feedback>();
    private int numTteFeedback;
    private int numPasFeedback;
    private List<Integer> randomSeatList;

    public String getCoachNumber() {
        return coachNumber;
    }

    public int getNumberofSeats() {
        return numberOfSeats;
    }

    public CoachType getCoachType() {
        return coachType;
    }

    public Coach(String cn, int nos, CoachType ct) {
        coachNumber = cn;
        numberOfSeats = nos;
        coachType = ct;
        initRandomSeats();
        numTteFeedback = 0;
        numPasFeedback = 0;
    }

    public void initRandomSeats() {
        int[]seatArray = new int[numberOfSeats];
        for (int i = 1; i<=numberOfSeats; i++)
            seatArray[i-1] = i;

        Util.randomizeArray(seatArray, numberOfSeats);
        randomSeatList = new ArrayList<Integer>();
        for (int i = 1; i<=numberOfSeats; i++)
            randomSeatList.add(seatArray[i-1]);
    }

    public Boolean isSeatAvailableForFeedback() {
        if(randomSeatList.size()>0)
            return true;

        return false;
    }

    //get a random seat number and delete it for later
    public int getRandomSeat() {
//        printSeats();
        if(randomSeatList.size()== 0)
            return 0;
        int size = randomSeatList.size();
        int seatNumber = randomSeatList.get(size - 1);
        randomSeatList.remove(size - 1);
        return seatNumber;
    }

    //debug methods
    public void printSeats() {
        Log.d("debugTag", "printing seats:");
        for(int seat : randomSeatList) {
            Log.d("debugTag", "" + seat);
        }
    }

    public void addFeedback(Feedback feedback) {
//        if(feedback.getFeedbackType() == Feedback.FeedbackType.TTE)
//            tteFeedback.add(feedback);
//        else if (feedback.getFeedbackType() == Feedback.FeedbackType.PASSENGER)
//            pasFeedback.add(feedback);
//        else
//            Log.e("debugTag", "wrong feedback type");

        if(feedback.getFeedbackType() == Feedback.FeedbackType.TTE) {
            numTteFeedback++;
        }
        else if (feedback.getFeedbackType() == Feedback.FeedbackType.PASSENGER) {
            numPasFeedback++;
        }
    }

    public void resetNumFeedbacks() {
        numTteFeedback = 0;
        numPasFeedback = 0;
    }
}