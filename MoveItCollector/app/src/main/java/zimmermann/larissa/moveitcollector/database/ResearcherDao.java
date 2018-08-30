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
public interface ResearcherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Researcher researcher);

    @Query("DELETE FROM researcher_table")
    public void deleteAll();

    @Delete
    public void delete(Researcher researcher);

    @Update
    public void update(Researcher researcher);

    @Query("SELECT * FROM researcher_table")
    public LiveData<List<Researcher>> getAllResearchers();

    @Query("SELECT * FROM researcher_table WHERE email = :email")
    public LiveData<Researcher> getResearcherById(final String email);
}
