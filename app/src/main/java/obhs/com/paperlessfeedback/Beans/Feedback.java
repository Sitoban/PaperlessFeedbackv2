package obhs.com.paperlessfeedback.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mannis on 01-Apr-18.
 */

public class Feedback {
    public enum FeedbackType {
        TTE_AC,
        TTE_NONAC,
        PASSENGER_AC,
        PASSENGER_NONAC
    }

    private FeedbackType feedbackType;
    private double psi;

    public Feedback(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
        psi = 0;
    }

}