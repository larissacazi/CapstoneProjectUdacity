package zimmermann.larissa.moveitcollector.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.Volunteer;

public class VolunteerActivityViewModel extends AndroidViewModel {
    private VolunteerActivityRepository mVolunteerActivityRepository;
    private LiveData<Volunteer> mVolunteer;
    private LiveData<List<Activity>> mAllActivities;

    public VolunteerActivityViewModel(@NonNull Application application) {
        super(application);
        mVolunteerActivityRepository = new VolunteerActivityRepository(application);
    }

    public void setVolunteerInfo(int projectId, String volunteerEmail) {
        mVolunteerActivityRepository.setVolunteerInfo(projectId, volunteerEmail);
        mVolunteer = mVolunteerActivityRepository.getVolunteer();
        mAllActivities = mVolunteerActivityRepository.getActivityList();
    }

    public LiveData<Volunteer> getVolunteer() {
        return mVolunteer;
    }

    public LiveData<List<Activity>> getAllActivities() {
        return mAllActivities;
    }
}
