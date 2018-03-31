package obhs.com.paperlessfeedback.ApplicationContext;

import android.app.Application;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.Beans.Trip;

/**
 * Created by mannis on 31-Mar-18.
 */

public class GlobalContext extends Application{
    private List<Train> trainsList = new ArrayList<Train>();
    private View mainDashboardView;
    private long currentTrainIndex;
    private Trip currentTrip;

    public long getCurrentTrainIndex() {
        return currentTrainIndex;
    }

    public void setCurrentTrainIndex(long currentTrainIndex) {
        this.currentTrainIndex = currentTrainIndex;
    }

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

    public void setMainDashboardView(View v) {
        mainDashboardView = v;
    }

    public View getMainDashboardView() {
        return mainDashboardView;
    }

//    public void setListOfTrains(List<Train> listOfTrains) {
//        this.listOfTrains = listOfTrains;
//    }

}
