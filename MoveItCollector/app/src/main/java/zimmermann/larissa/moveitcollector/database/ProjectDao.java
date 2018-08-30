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
public interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Project project);

    @Query("DELETE FROM project_table")
    public void deleteAll();

    @Delete
    public void delete(Project project);

    @Update
    public void update(Project project);

    @Query("SELECT * FROM project_table WHERE project_id = :id")
    public LiveData<Project> getProjectById(int id);

    @Query("SELECT * FROM project_table ORDER BY name ASC")
    public LiveData<List<Project>> getAllProjects();
}
