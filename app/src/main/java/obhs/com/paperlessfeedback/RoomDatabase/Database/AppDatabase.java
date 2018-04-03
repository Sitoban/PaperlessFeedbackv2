package obhs.com.paperlessfeedback.RoomDatabase.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import obhs.com.paperlessfeedback.RoomDatabase.Dao.FeedbackObjDao;
import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

/**
 * Created by mannis on 02-Apr-18.
 */

@Database(entities = {FeedbackObj.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract FeedbackObjDao feedbackObjDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "feedbckObj-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
