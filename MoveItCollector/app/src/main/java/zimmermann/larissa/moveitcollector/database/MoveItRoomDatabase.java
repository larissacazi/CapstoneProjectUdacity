package zimmermann.larissa.moveitcollector.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.utils.Converters;


@Database(entities = {Researcher.class, Project.class, Activity.class, Volunteer.class},
          version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MoveItRoomDatabase extends RoomDatabase {

    private static MoveItRoomDatabase instance;

    public abstract ActivityDao activityDao();
    public abstract ProjectDao projectDao();
    public abstract ResearcherDao researcherDao();
    public abstract VolunteerDao volunteerDao();

    public static MoveItRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (MoveItRoomDatabase.class) {
                if (instance == null) {
                    //Create the database
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MoveItRoomDatabase.class,
                            context.getResources().getString(R.string.move_it_database_name))
                            //.addCallback(sCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;
    }
}
