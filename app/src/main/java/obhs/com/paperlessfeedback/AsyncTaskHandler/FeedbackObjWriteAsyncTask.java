package obhs.com.paperlessfeedback.AsyncTaskHandler;

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
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

import static obhs.com.paperlessfeedback.Util.Util.setupCustomTrain;

/**
 * Created by mannis on 31-Mar-18.
 */

public class FeedbackObjWriteAsyncTask extends AsyncTask<AppDatabase, Void, Boolean> {
    private FeedbackObj feedbackObj;

    public FeedbackObjWriteAsyncTask(FeedbackObj feedbackObj) {
        this.feedbackObj = feedbackObj;
    }
    @Override
    protected Boolean doInBackground(AppDatabase... appDatabases) {
        AppDatabase db = (AppDatabase)appDatabases[0];

        int count = db.feedbackObjDao().countFeedbackObj();
//        Log.d("debugTag", "count: " + count);
//        db.feedbackObjDao().delAll();

        db.feedbackObjDao().insertAll(feedbackObj);
        return null;
    }
}
