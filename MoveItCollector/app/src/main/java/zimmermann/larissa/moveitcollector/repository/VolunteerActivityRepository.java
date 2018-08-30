package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.ActivityDao;
import zimmermann.larissa.moveitcollector.database.MoveItRoomDatabase;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.database.VolunteerDao;

public class VolunteerActivityRepository {
    private VolunteerDao mVolunteerDao;
    private ActivityDao mActivityDao;
    private LiveData<Volunteer> mVolunteer;
    private LiveData<List<Activity>> mActivityList;

    private int mProjectId;
    private String mVolunteerEmail;

    public VolunteerActivityRepository(Application application) {
        MoveItRoomDatabase db = MoveItRoomDatabase.getDatabase(application);
        mVolunteerDao = db.volunteerDao();
        mActivityDao = db.activityDao();
    }

    public void setVolunteerInfo(int projectId, String volunteerEmail) {
        mProjectId = projectId;
        mVolunteerEmail = volunteerEmail;
        mVolunteer = mVolunteerDao.getVolunteerById(mProjectId, mVolunteerEmail);
        mActivityList = mActivityDao.findActivitiesForProject(projectId);
    }

    public LiveData<Volunteer> getVolunteer() {
        return mVolunteer;
    }

    public LiveData<List<Activity>> getActivityList() {
        return mActivityList;
    }
}
