package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.events.SingleLiveEvent;

public class MainActivityViewModel extends AndroidViewModel {
    private MainActivityRepository mMainActivityRepository;
    private LiveData<List<Project>> mAllProjects;

    private final SingleLiveEvent<Project> mOpenProjectEvent = new SingleLiveEvent<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mMainActivityRepository = new MainActivityRepository(application);
        mAllProjects = mMainActivityRepository.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }

    public SingleLiveEvent<Project> getOpenProjectEvent() {
        return mOpenProjectEvent;
    }

    public void insert(Project project) {
        mMainActivityRepository.insert(project);
    }

    public void delete(Project project) {
        mMainActivityRepository.delete(project);
    }
}
