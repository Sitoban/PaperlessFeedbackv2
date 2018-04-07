package obhs.com.paperlessfeedback.AsyncTaskHandler;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.DashboardFragments.DashboardFragment;
import obhs.com.paperlessfeedback.R;

import static obhs.com.paperlessfeedback.Util.Util.setupCustomTrain;

/**
 * Created by mannis on 31-Mar-18.
 */

public class SetPendingEntriesAsyncTask extends AsyncTask<GlobalContext, Void, Integer> {
    private Fragment fragment;

    public SetPendingEntriesAsyncTask(Fragment fragment) {
        this.fragment = fragment;
    }
    @Override
    protected Integer doInBackground(GlobalContext... globalContexts) {
        GlobalContext globalContext = globalContexts[0];
        int  numEntries = globalContext.getDb().feedbackObjDao().countFeedbackObj();
        globalContext.setNumLocalDbFeedbacks(numEntries);
        return numEntries;
    }

    //edit: change from Boolean to void
    @Override
    protected void onPostExecute(Integer n) {
        ((DashboardFragment)fragment).setNumEntriesLocal(n);
    }
}
