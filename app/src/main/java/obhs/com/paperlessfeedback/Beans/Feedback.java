package obhs.com.paperlessfeedback.Beans;

import android.util.Log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.Util.Util;

/**
 * Created by mannis on 01-Apr-18.
 */

public class Feedback {

    //edit: correct all four stringArrays

    //AC - Passenger
    final static String[] feedbackQuestionsType1 = new String[] {
            "Question 1 of 5: Cleaning of Toilets(Including toilet floor, commode pan, wall panels, shelf, mirror, wash basin, Disinfection and provision of deodorant etc.",
            "Question 2 of 5: Cleaning  of Passenger Compartment (Including cleaning of passenger aisle, Vestibule areas, Doorway area and Doorway wash basin, spraying of air freshner and cleaning of dustbin)",
            "Question 3 of 5: Collection of garbage from the coach compartments and clearance of dustbins.",
            "Question 4 of 5: Spraying of Mosquito/Cockroach/Fly Repellent and Providing Glue Board whenever required or on demand by passengers.",
            "Question 5 of 5: Behaviour/Response of Janitors/Supervisor (Including hygiene & cleanliness of Janitor/Supervisor)"
    };

    //NON_AC - Passenger
    final static String[] feedbackQuestionsType2 = new String[] {
            "Question 1 of 3: Cleaning of Toilets, Wash Basin and other fittings",
            "Question 2 of 3: Complete Cleaning of Passenger Compartment",
            "Question 3 of 3: Behavior of Janitors/supervisor including hygiene & cleanliness of janitor/Supervisor "
    };

    //AC - TTE
    final static String[] feedbackQuestionsType3 = new String[] {
            "Question 1 of 5: Cleaning of Toilets(Including toilet floor, commode pan, wall panels, shelf, mirror, wash basin, Disinfection and provision of deodorant etc.",
            "Question 2 of 5: Cleaning  of Passenger Compartment (Including cleaning of passenger aisle, Vestibule areas, Doorway area and Doorway wash basin, spraying of air freshner and cleaning of dustbin)",
            "Question 3 of 5: Collection of garbage from the coach compartments and clearance of dustbins.",
            "Question 4 of 5: Spraying of Mosquito/Cockroach/Fly Repellent and Providing Glue Board whenever required or on demand by passengers.",
            "Question 5 of 5: Behaviour/Response of Janitors/Supervisor (Including hygiene & cleanliness of Janitor/Supervisor)"
    };

    //NON_AC -TTE
    final static String[] feedbackQuestionsType4 = new String[] {
            "Question 1 of 3: Cleaning of Toilets, Wash Basin and other fittings",
            "Question 2 of 3: Complete Cleaning of Passenger Compartment",
            "Question 3 of 3: Behavior of Janitors/supervisor including hygiene & cleanliness of janitor/Supervisor "
    };

    public enum FeedbackType implements Serializable{
//        TTE_AC,
//        TTE_NONAC,
//        PASSENGER_AC,
//        PASSENGER_NONAC
        TTE,
        PASSENGER
    }

    private FeedbackType feedbackType;
    private Coach.CoachType coachType;
    private double psi;
    private String[] currentFeedbackQuestions;
    private int currentQuestionIndex;
    private double score;
    private boolean applyPenalty;

    public Feedback(FeedbackType feedbackType, Coach.CoachType coachType) {
        this.feedbackType = feedbackType;
        this.coachType = coachType;
        psi = 0;
        score = 0;
        currentQuestionIndex = -1;
        applyPenalty = false;
        //call after setting feedbackType and coachType
        setFeedbackQuestions();
    }

    public void setFeedbackQuestions() {
        if (feedbackType == FeedbackType.PASSENGER && coachType == Coach.CoachType.AC) {
            currentFeedbackQuestions = feedbackQuestionsType1;
        } else if (feedbackType == FeedbackType.PASSENGER && coachType == Coach.CoachType.NON_AC) {
            currentFeedbackQuestions = feedbackQuestionsType2;
        } else if (feedbackType == FeedbackType.TTE && coachType == Coach.CoachType.AC) {
            currentFeedbackQuestions = feedbackQuestionsType3;
        } else if (feedbackType == FeedbackType.TTE && coachType == Coach.CoachType.NON_AC) {
            currentFeedbackQuestions = feedbackQuestionsType4;
        } else {
            Log.e("debugTag", "incorrect feedback type or coach type");
        }
    }

    private Boolean isQuestionIndexValid(int index) {
        int numQ = currentFeedbackQuestions.length;
        if( (index > -1) && ((numQ - 1) >= index) ) {
            return true;
        }
        return false;
    }

    //returns null if already on the last question
    public String getNextQuestion() {
        String result = null;
        int nextQuestionIndex = currentQuestionIndex + 1;
        if(isQuestionIndexValid(nextQuestionIndex)) {
            currentQuestionIndex = nextQuestionIndex;
            result = currentFeedbackQuestions[nextQuestionIndex];
        }
        return result;
    }

    public Boolean isAtLastQuestion() {
        if ( currentQuestionIndex == (currentFeedbackQuestions.length - 1) ) {
            return true;
        }
        return false;
    }

    public void addScoreByRating(String rating) {
        double feedbackPoint = 0;
        switch (rating) {
            case "Excellent":
                feedbackPoint = 1;
                break;
            case "Very Good":
                feedbackPoint = .9;
                break;
            case "Good":
                feedbackPoint = .8;
                break;
            case "Average":
                feedbackPoint = .5;
                break;
            case "Poor":
                feedbackPoint = .2;
                break;
        }
        score += feedbackPoint;
        //fixing 1.6 + 0.8 = 2.4000000000004 issue
        //edit: confirm usage of RoundingMode.HALF_UP
        score = BigDecimal.valueOf(score)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        if(currentQuestionIndex == 1)
        {
            applyPenalty = score <= 1.2;
            Util.logd("applying penalty for score : "+score);
        }
    }

    public void calculatePsi() {

        psi = (score / currentFeedbackQuestions.length) * 100;
        psi = BigDecimal.valueOf(psi)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        if(psi < 50 || applyPenalty)
        {
            psi = 0;
            Util.logd("setting psi to 0 when score : "+score+" < 1.5 or applyPenalty is true");
        }
    }

    public double getPsi() {
        return psi;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }
}