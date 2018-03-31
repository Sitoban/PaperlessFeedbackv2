package obhs.com.paperlessfeedback.AsyncTaskHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import obhs.com.paperlessfeedback.ApplicationContext.GlobalContext;
import obhs.com.paperlessfeedback.Beans.Coach;
import obhs.com.paperlessfeedback.Beans.Train;
import obhs.com.paperlessfeedback.R;

/**
 * Created by mannis on 31-Mar-18.
 */

public class AsyncTaskUtil {

    //task for setting up the train list
//    static public AsyncTask<GlobalContext, Void, GlobalContext> getDatabaseReadAsyncTask() {
    static public ContextualAsyncTask getDatabaseReadAsyncTask(Context context) {
        return new ContextualAsyncTask(context);
    }
}
