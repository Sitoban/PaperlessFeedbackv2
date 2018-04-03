package obhs.com.paperlessfeedback.RoomDatabase.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import obhs.com.paperlessfeedback.RoomDatabase.Entity.FeedbackObj;

/**
 * Created by mannis on 02-Apr-18.
 */

@Dao
public interface FeedbackObjDao {
    @Query("SELECT * FROM feedbackObj")
    List<FeedbackObj> getAll();

    @Query("SELECT COUNT(*) from feedbackObj")
    int countFeedbackObj();

    @Query("DELETE FROM feedbackObj")
    void dropTable();

    @Insert
    void insertAll(FeedbackObj... feedbackObjs);

    @Delete
    void delete(FeedbackObj feedbackObj);

}
