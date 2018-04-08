package obhs.com.paperlessfeedback.RoomDatabase.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by mannis on 02-Apr-18.
 */

@Entity(tableName = "feedbackObj")
public class FeedbackObj {

    @PrimaryKey(autoGenerate = true)
    private int fid;

    @ColumnInfo(name = "trip_id")
    private String tripId;

    @ColumnInfo(name = "train_number")
    private String trainNumber;

    @ColumnInfo(name = "trip_date")
    private String tripStartDate;

    @ColumnInfo(name = "pnr")
    private String pnr;

    @ColumnInfo(name = "mobile_number")
    private String mobileNumber;

    @ColumnInfo(name = "coach_number")
    private String coachNumber;

    @ColumnInfo(name = "seat_number")
    private String seatNumber;

    @ColumnInfo(name = "psi")
    private String psi;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] faceImage;

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getFid() {
        return fid;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPsi() {
        return psi;
    }

    public void setPsi(String psi) {
        this.psi = psi;
    }

//    public Bitmap getFaceImage()  {
//        return this.faceImage;
//    }
//    public void setFaceImage(Bitmap image) {this.faceImage = image;}

    public byte[] getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public String getQueryString()
    {
        return "PSI="+psi+"&CoachNumber="+coachNumber+"&SeatNumber="+seatNumber+"&MobileNumber="+mobileNumber+"&TrainNumber="+trainNumber+"&Date="+tripStartDate+"&TripID="+tripId+"&PNRNumber="+pnr;
    }

}