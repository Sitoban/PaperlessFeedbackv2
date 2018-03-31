package obhs.com.paperlessfeedback.Beans;

import java.util.Date;

/**
 * Created by mannis on 31-Mar-18.
 */

public class Trip {

    public enum TripStatus {
        GOING,
        ARRIVING
    }

    private long tripId;
    private Train train;
    private Date startTime;
    private Date midTime;
    private Date endTime;
    private TripStatus tripStatus;

    public Trip(Train t) {
        //edit: initialize tripId'
        train = t;
        startTime = new Date();
        tripStatus = TripStatus.GOING;
    }
}
