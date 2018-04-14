package obhs.com.paperlessfeedback.Beans;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.Util.Util;

import static obhs.com.paperlessfeedback.Util.Util.logd;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Coach {
    public enum CoachType {
        AC,
        NON_AC;
    }
    private final static int NUM_FEEDBACK_PER_COACH_PER_TYPE = 2;
    private String coachName;
    private int numberOfSeats;
    private CoachType coachType;
    //    private List<Feedback> tteFeedback= new ArrayList<Feedback>();
//    private List<Feedback> pasFeedback= new ArrayList<Feedback>();
    private int numTteFeedback;
    private int numPasFeedback;
    private List<Integer> randomSeatList;

    public boolean isFeedbackPending(Feedback.FeedbackType feedbackType) {
        boolean result = true;
        if(feedbackType == Feedback.FeedbackType.TTE) {
            if(numTteFeedback >=NUM_FEEDBACK_PER_COACH_PER_TYPE) {
                result = false;
            }
        }
        else if (feedbackType == Feedback.FeedbackType.PASSENGER) {
            if(numPasFeedback >=NUM_FEEDBACK_PER_COACH_PER_TYPE) {
                result = false;
            }
        }
        return result;
    }

    public String getCoachName() {
        return coachName;
    }

    public int getNumberofSeats() {
        return numberOfSeats;
    }

    public CoachType getCoachType() {
        return coachType;
    }

    public Coach(String cn, int nos, CoachType ct) {
        coachName = cn;
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

    public void setCoachType(List<Integer> randomSeatList) {
        this.randomSeatList = randomSeatList;
    }

    public Boolean isSeatAvailableForFeedback() {
        if(randomSeatList.size()>0)
            return true;

        return false;
    }

    public String getRandString() {
        String randString = "";
        int size = randomSeatList.size();
        int index = 0;
        for(int randNum : randomSeatList) {
            randString += randNum;
            if(index != size -1) {
                randString += ":";
            }
            index++;
        }
        logd("randString for Coach " + getCoachName() + " : " + randString);
        return randString;
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
            Log.d("debugTag", "ptr: " + this + " ,coach: " + getCoachName() + " , numFeed: " + getNumPasFeedback());
        }
    }

    public void resetNumFeedbacks() {
        numTteFeedback = 0;
        numPasFeedback = 0;
    }

    public int getNumTteFeedback() {
        return numTteFeedback;
    }

    public void setNumTteFeedback(int numTteFeedback) {
        this.numTteFeedback = numTteFeedback;
    }

    public int getNumPasFeedback() {
        return numPasFeedback;
    }

    public void setNumPasFeedback(int numPasFeedback) {
        this.numPasFeedback = numPasFeedback;
    }
}