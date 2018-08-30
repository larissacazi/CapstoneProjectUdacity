package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.ActivityDao;
import zimmermann.larissa.moveitcollector.database.MoveItRoomDatabase;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.database.ProjectDao;
import zimmermann.larissa.moveitcollector.database.Researcher;
import zimmermann.larissa.moveitcollector.database.VolunteerDao;

public class MainActivityRepository {
    private ProjectDao mProjectDao;
    private ActivityDao mActivityDao;
    private VolunteerDao mVolunteerDao;
    private LiveData<List<Project>> mAllProjects;
    private LiveData<List<Researcher>> mReseacherList;

    public MainActivityRepository(Application application) {
        MoveItRoomDatabase db = MoveItRoomDatabase.getDatabase(application);
        mProjectDao = db.projectDao();
        mActivityDao = db.activityDao();
        mVolunteerDao = db.volunteerDao();
        mAllProjects = mProjectDao.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }

    public void insert(Project project) {
        new InsertAsyncTask(mProjectDao).execute(project);
    }

    public void delete(Project project) {
        new DeleteProjectAsyncTask(mProjectDao, mActivityDao, mVolunteerDao).execute(project);
    }

    private static class InsertAsyncTask extends AsyncTask<Project, Void, Void> {

        private ProjectDao mAsyncTaskDao;

        public InsertAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            mAsyncTaskDao.insert(projects[0]);
            return null;
        }
    }

    private static class DeleteProjectAsyncTask extends AsyncTask<Project, Void, Void> {

        private ProjectDao mAsyncTaskDao;
        private VolunteerDao mAsyncVolunteerDao;
        private ActivityDao mAsyncActivityDao;

        public DeleteProjectAsyncTask(ProjectDao dao, ActivityDao activityDao, VolunteerDao volunteerDao) {
            mAsyncTaskDao = dao;
            mAsyncActivityDao = activityDao;
            mAsyncVolunteerDao = volunteerDao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            mAsyncActivityDao.deleteActivities(projects[0].getProjectId());
            mAsyncVolunteerDao.deleteVolunteers(projects[0].getProjectId());
            mAsyncTaskDao.delete(projects[0]);
            return null;
        }
    }

}
