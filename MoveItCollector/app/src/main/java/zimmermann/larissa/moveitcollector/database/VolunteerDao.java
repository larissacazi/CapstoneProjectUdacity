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
public interface VolunteerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Volunteer volunteer);

    @Delete
    public void delete(Volunteer... volunteers);

    @Query("DELETE FROM volunteer_table WHERE project_id = :projectId")
    public void deleteVolunteers(int projectId);

    @Update
    public void update(Volunteer volunteer);

    @Query("SELECT * FROM volunteer_table")
    public LiveData<List<Volunteer>> getAllVolunteers();

    @Query("SELECT * FROM volunteer_table WHERE project_id = :projectId")
    public LiveData<List<Volunteer>> findVolunteersForProject(final int projectId);

    @Query("SELECT * FROM volunteer_table WHERE email = :email AND project_id = :projectId")
    public LiveData<Volunteer> getVolunteerById(final int projectId, final String email);
}
