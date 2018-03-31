package obhs.com.paperlessfeedback.ApplicationContext;

import android.app.Application;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.Beans.Train;

/**
 * Created by mannis on 31-Mar-18.
 */

public class GlobalContext extends Application{
    private List<Train> trainsList = new ArrayList<Train>();
    private View mainDashboardView;
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
