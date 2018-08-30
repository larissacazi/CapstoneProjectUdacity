package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import zimmermann.larissa.moveitcollector.database.MoveItRoomDatabase;
import zimmermann.larissa.moveitcollector.database.Researcher;
import zimmermann.larissa.moveitcollector.database.ResearcherDao;

public class LoginActivityRepository {
    private ResearcherDao mResearcherDao;
    private LiveData<List<Researcher>> mReseacherList;

    public LoginActivityRepository(Application application) {
        MoveItRoomDatabase db = MoveItRoomDatabase.getDatabase(application);
        mResearcherDao = db.researcherDao();
        mReseacherList = mResearcherDao.getAllResearchers();
    }

    public LiveData<List<Researcher>> getReseacherList() {
        return mReseacherList;
    }

    public void insert(Researcher researcher) {
        new InsertResearcherAsyncTask(mResearcherDao).execute(researcher);
    }

    public void delete(Researcher researcher) {
        new DeleteResearcherAsyncTask(mResearcherDao).execute(researcher);
    }

    public void update(Researcher researcher) {
        new UpdateResearcherAsyncTask(mResearcherDao).execute(researcher);
    }

    private static class InsertResearcherAsyncTask extends AsyncTask<Researcher, Void, Void> {

        private ResearcherDao mAsyncTaskDao;

        public InsertResearcherAsyncTask(ResearcherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Researcher... researchers) {
            mAsyncTaskDao.insert(researchers[0]);
            return null;
        }
    }

    private static class DeleteResearcherAsyncTask extends AsyncTask<Researcher, Void, Void> {

        private ResearcherDao mAsyncTaskDao;

        public DeleteResearcherAsyncTask(ResearcherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Researcher... researchers) {
            mAsyncTaskDao.delete(researchers[0]);
            return null;
        }
    }

    private static class UpdateResearcherAsyncTask extends AsyncTask<Researcher, Void, Void> {

        private ResearcherDao mAsyncTaskDao;

        public UpdateResearcherAsyncTask(ResearcherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Researcher... researchers) {
            mAsyncTaskDao.update(researchers[0]);
            return null;
        }
    }
}
