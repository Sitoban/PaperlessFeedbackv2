package obhs.com.paperlessfeedback.ApplicationContext;

import android.app.Application;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.Beans.Trip;
import obhs.com.paperlessfeedback.DashboardFragments.DashboardFragment;
import obhs.com.paperlessfeedback.DashboardFragments.TrainSelectionFragment;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;

/**
 * Created by mannis on 31-Mar-18.
 */

public class GlobalContext extends Application{
    private List<Train> trainsList = new ArrayList<Train>();
//    private View mainDashboardView;
    private long currentTrainIndex;
    private Trip currentTrip;
    private Coach currentLiveCoach;
    private int numLocalDbFeedbacks;
    private static AppDatabase db;
    private DashboardFragment liveDashboardFragment;
    private TrainSelectionFragment liveTrainSelectionFragment;

    Set<String> usedMobileNumbers = new HashSet<String>();

    public long getCurrentTrainIndex() {
        return currentTrainIndex;
    }

    public void setCurrentTrainIndex(long currentTrainIndex) { this.currentTrainIndex = currentTrainIndex; }

    public Train getCurrentTrain() {
        return trainsList.get((int)currentTrainIndex);
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

//    private List<Train> trainsList;

    public List<Train> getListOfTrains() {
        return trainsList;
    }

    public void addTrain(Train t) {
        trainsList.add(t);
    }

//    public void setMainDashboardView(View v) { mainDashboardView = v; }
//
//    public View getMainDashboardView() {
//        return mainDashboardView;
//    }

//    public void setListOfTrains(List<Train> listOfTrains) {
//        this.listOfTrains = listOfTrains;
//    }

    public AppDatabase getDb() { return db; }

    public void setDb(AppDatabase db) {
        GlobalContext.db = db;
    }

    public int getNumLocalDbFeedbacks() {
        return numLocalDbFeedbacks;
    }

    public void setNumLocalDbFeedbacks(int numLocalDbFeedbacks) {
        this.numLocalDbFeedbacks = numLocalDbFeedbacks;
    }

    public DashboardFragment getLiveDashboardFragment() {
        return liveDashboardFragment;
    }

    public void setLiveDashboardFragment(DashboardFragment liveDashboardFragment) {
        this.liveDashboardFragment = liveDashboardFragment;
    }

    public Coach getCurrentLiveCoach() {
        return currentLiveCoach;
    }

    public void setCurrentLiveCoach(Coach currentLiveCoach) {
        this.currentLiveCoach = currentLiveCoach;
    }

    public TrainSelectionFragment getLiveTrainSelectionFragment() {
        return liveTrainSelectionFragment;
    }

    public void setLiveTrainSelectionFragment(TrainSelectionFragment liveTrainSelectionFragment) {
        this.liveTrainSelectionFragment = liveTrainSelectionFragment;
    }

    public void addUsedMobileNumber(String mobileNumber) {
        usedMobileNumbers.add(mobileNumber);
    }

    public boolean isMobileNumberUsed(String mobileNumber) {
        boolean result = false;
        if(usedMobileNumbers.contains(mobileNumber))
            result = true;
        return result;
    }

    public void clearUsedMobileNumbers() {
        usedMobileNumbers.clear();
    }
}
