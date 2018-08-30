package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.ActivityDao;
import zimmermann.larissa.moveitcollector.database.MoveItRoomDatabase;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.database.ProjectDao;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.database.VolunteerDao;

public class ProjectActivityRepository {
    private VolunteerDao mVolunteerDao;
    private ProjectDao mProjectDao;
    private ActivityDao mActivityDao;
    private LiveData<List<Activity>> mAllActivities = null;
    private LiveData<List<Volunteer>> mAllVolunteers = null;
    private LiveData<Project> mProject;
    private int mProjectId;

    public ProjectActivityRepository(Application application) {
        MoveItRoomDatabase db = MoveItRoomDatabase.getDatabase(application);
        mActivityDao = db.activityDao();
        mProjectDao = db.projectDao();
        mVolunteerDao = db.volunteerDao();
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
        mAllActivities = mActivityDao.findActivitiesForProject(projectId);
        mProject = mProjectDao.getProjectById(projectId);
        mAllVolunteers = mVolunteerDao.findVolunteersForProject(projectId);
    }

    public LiveData<Project> getProject() {
        return mProject;
    }

    public LiveData<List<Activity>> getAllActivitiesFromProject(int projectId) {
        if (mAllActivities == null) setProjectId(projectId);
        return mAllActivities;
    }

    public LiveData<List<Volunteer>> getAllVolunteersFromProject(int projectId) {
        if (mAllVolunteers == null) setProjectId(projectId);
        return mAllVolunteers;
    }

    public void insertActivity(Activity activity) {
        new InsertActivityAsyncTask(mActivityDao).execute(activity);
    }

    public void deleteActivity(Activity activity) {
        new DeleteActivityAsyncTask(mActivityDao).execute(activity);
    }

    public void insertVolunteer(Volunteer volunteer) {
        new InsertVolunteerAsyncTask(mVolunteerDao).execute(volunteer);
    }

    public void deleteVolunteer(Volunteer volunteer) {
        new DeleteVolunteerAsyncTask(mVolunteerDao).execute(volunteer);
    }

    private static class InsertActivityAsyncTask extends AsyncTask<Activity, Void, Void> {

        private ActivityDao mAsyncTaskDao;

        public InsertActivityAsyncTask(ActivityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            mAsyncTaskDao.insert(activities[0]);
            return null;
        }
    }

    private static class DeleteActivityAsyncTask extends AsyncTask<Activity, Void, Void> {

        private ActivityDao mAsyncTaskDao;

        public DeleteActivityAsyncTask(ActivityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            mAsyncTaskDao.delete(activities[0]);
            return null;
        }
    }

    private static class InsertVolunteerAsyncTask extends AsyncTask<Volunteer, Void, Void> {

        private VolunteerDao mAsyncTaskDao;

        public InsertVolunteerAsyncTask(VolunteerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Volunteer... volunteers) {
            mAsyncTaskDao.insert(volunteers[0]);
            return null;
        }
    }

    private static class DeleteVolunteerAsyncTask extends AsyncTask<Volunteer, Void, Void> {

        private VolunteerDao mAsyncTaskDao;

        public DeleteVolunteerAsyncTask(VolunteerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Volunteer... volunteers) {
            mAsyncTaskDao.delete(volunteers[0]);
            return null;
        }
    }
}
