package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.events.SingleLiveEvent;

public class ProjectActivityViewModel extends AndroidViewModel {
    private ProjectActivityRepository mProjectActivityRepository;
    private LiveData<List<Activity>> mAllActivities;
    private LiveData<List<Volunteer>> mAllVolunteers;
    private LiveData<Project> mProject;
    private int mProjectId;

    private final SingleLiveEvent<String> mOpenVolunteerEvent = new SingleLiveEvent<>();

    public ProjectActivityViewModel(@NonNull Application application) {
        super(application);
        mProjectActivityRepository = new ProjectActivityRepository(application);
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
        mAllActivities = mProjectActivityRepository.getAllActivitiesFromProject(projectId);
        mProject = mProjectActivityRepository.getProject();
        mAllVolunteers = mProjectActivityRepository.getAllVolunteersFromProject(projectId);
    }

    public LiveData<List<Activity>> getAllActivities(int projectId) {
        setProjectId(projectId);
        return mAllActivities;
    }

    public LiveData<Project> getProject() {
        return mProject;
    }

    public void insertActivity(Activity activity) {
        mProjectActivityRepository.insertActivity(activity);
    }

    public void deleteActivity(Activity activity) {
        mProjectActivityRepository.deleteActivity(activity);
    }

    public void insertVolunteer(Volunteer volunteer) {
        mProjectActivityRepository.insertVolunteer(volunteer);
    }

    public void deleteVolunteer(Volunteer volunteer) {
        mProjectActivityRepository.deleteVolunteer(volunteer);
    }

    public LiveData<List<Volunteer>> getAllVolunteers(int projectId) {
        return mAllVolunteers;
    }

    public SingleLiveEvent<String> getOpenVolunteerEvent() {
        return mOpenVolunteerEvent;
    }
}
