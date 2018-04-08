package obhs.com.paperlessfeedback.Beans;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Trip {

    public enum TripStatus {
        GOING,
        ARRIVING,
        END
    }

    private long tripId;
    private Train train;
    private Date startTime;
    private Date midTime;
    private Date endTime;

    private TripStatus tripStatus;

    private void createTripId() {
        String tripIdString = new SimpleDateFormat("yyyyMMdd").format(startTime) + train.getTrainNumber();
        tripId = Long.parseLong(tripIdString);
//        Log.d("debugTag", "tripId: "+ tripId);
    }

    public Trip(Train t) {
        train = t;
        startTime = new Date();
        tripStatus = TripStatus.GOING;
        createTripId();
    }

    public void setNextTripState() {
        switch (tripStatus) {
            case GOING:
                tripStatus = TripStatus.ARRIVING;
                break;
            case ARRIVING:
//                tripStatus = TripStatus.END;
//                break;
//            case END:
                Log.e("debugTag","Unexpected code execution");
                   break;
        }
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public long getTripId() {
        return  tripId;
    }

    public Train getTrain() {
        return train;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getNumCompletedFeedbacks() {
        //edit: make change for tte support
//        int numTotal = getTrain().getNumRequiredFeedbacks();
        int numCompleted = 0;
        for(Coach coach:getTrain().getCoachList()) {
//            Log.d("debugTag", "numFeed " + coach.getCoachNumber() + ": " + coach.getNumPasFeedback());
            numCompleted += coach.getNumPasFeedback();
        }
        return numCompleted;
    }

    public int getNumPendingFeedbacks() {
        int numTotal = getTrain().getNumRequiredFeedbacks();
        int numPending = numTotal -  getNumCompletedFeedbacks();
        return numPending;
    }
}
