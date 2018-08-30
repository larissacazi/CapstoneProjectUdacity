package zimmermann.larissa.moveitcollector.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Activity activity);

    @Query("DELETE FROM project_table")
    public void deleteAll();

    @Update
    public void update(Activity activity);

    @Delete
    public void delete(Activity activity);

    @Query("DELETE FROM activity_table WHERE project_id = :projectId")
    public void deleteActivities(int projectId);

    @Query("SELECT * FROM activity_table")
    public LiveData<List<Activity>> getAllActivities();

    @Query("SELECT * FROM activity_table WHERE project_id = :projectId")
    public LiveData<List<Activity>> findActivitiesForProject(final int projectId);
}
