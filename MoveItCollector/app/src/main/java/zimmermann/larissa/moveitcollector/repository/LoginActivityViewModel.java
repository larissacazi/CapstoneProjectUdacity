package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Researcher;

public class LoginActivityViewModel extends AndroidViewModel {

    private LoginActivityRepository mLoginActivityRepository;
    private LiveData<List<Researcher>> mReseacherList;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mLoginActivityRepository = new LoginActivityRepository(application);
        mReseacherList = mLoginActivityRepository.getReseacherList();
    }

    public LiveData<List<Researcher>> getReseacherList() {
        return mReseacherList;
    }

    public void insert(Researcher researcher) {
        mLoginActivityRepository.insert(researcher);
    }

    public void delete(Researcher researcher) {
        mLoginActivityRepository.delete(researcher);
    }

    public void update(Researcher researcher) {
        mLoginActivityRepository.update(researcher);
    }
}
