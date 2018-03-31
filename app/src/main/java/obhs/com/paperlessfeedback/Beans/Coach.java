package obhs.com.paperlessfeedback.Beans;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Coach {
    public enum CoachType {
        AC,
        NON_AC;
    }

    private String coachNumber;
    private int numberOfSeats;
    private CoachType coachType;

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
    }
}