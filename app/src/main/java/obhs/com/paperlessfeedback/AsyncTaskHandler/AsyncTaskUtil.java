package obhs.com.paperlessfeedback.AsyncTaskHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.R;
import obhs.com.paperlessfeedback.RoomDatabase.Database.AppDatabase;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

/**
 * Created by mannis on 31-Mar-18.
 */

public class AsyncTaskUtil {

    //task for setting up the train list
//    static public AsyncTask<GlobalContext, Void, GlobalContext> getContextualAsyncTask() {
    public static ContextualAsyncTask getContextualAsyncTask(Context context) {
        return new ContextualAsyncTask(context);
    }

    public static FeedbackObjWriteAsyncTask getFeedbackObjWriteAsyncTask(FeedbackObj feedbackObj) {
        return new FeedbackObjWriteAsyncTask(feedbackObj);
    }

    public static AsyncTask<AppDatabase, Void, Boolean> getDatabaseReadAsyncTask() {
        return new AsyncTask<AppDatabase, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(AppDatabase... appDatabases) {
                AppDatabase db = appDatabases[0];
                List<FeedbackObj> feedbackObjs = db.feedbackObjDao().getAll();
                ////////edit: delete
                Log.d("debugTag", "printFeedbackList");
                for(FeedbackObj feedbackObj: feedbackObjs) {
                    Log.d("debugTag", "tripId: "  + feedbackObj.getTripId() + "seat number: " + feedbackObj.getSeatNumber());
                }
                //////////edit: delete above
                return null;
            }
        };
    }
}
