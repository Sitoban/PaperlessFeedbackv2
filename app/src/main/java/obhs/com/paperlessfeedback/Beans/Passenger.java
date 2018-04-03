package obhs.com.paperlessfeedback.Beans;

/**
 * Created by mannis on 03-Apr-18.
 */

public class Passenger {
    public String getMobileNumber() {
        return mobileNumber;
    }

//    public void setMobileNumber(String mobileNumber) {
//        this.mobileNumber = mobileNumber;
//    }

    public String getPnr() {
        return pnr;
    }

//    public void setPnr(String pnr) {
//        this.pnr = pnr;
//    }

    public Passenger(String mobileNumber, String pnr) {
        this.mobileNumber = mobileNumber;
        this.pnr = pnr;
    }

    private String mobileNumber;
    private String pnr;
}
