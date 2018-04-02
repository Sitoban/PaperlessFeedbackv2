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
        //edit: initialize tripId'
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
                tripStatus = TripStatus.END;
                break;
            case END:
                Log.e("debugTag","Unexpected code execution");
                   break;
        }
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }
}
