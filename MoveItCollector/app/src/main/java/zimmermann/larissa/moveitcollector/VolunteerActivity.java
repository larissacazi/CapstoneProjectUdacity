package zimmermann.larissa.moveitcollector;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zimmermann.larissa.moveitcollector.adapter.ActivityVolunteerAdapter;
import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.repository.VolunteerActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.DataUtils;

public class VolunteerActivity extends AppCompatActivity {
    public static final String EXTRA_VOLUNTEER_EMAIL = "EXTRA_VOLUNTEER_EMAIL";
    public static final String EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID";

    private String mVolunteerEmail;
    private int mProjectId;
    private Volunteer mVolunteer;

    private List<Activity> mActivityList = null;

    @BindView(R.id.tvVolunteerNameField)
    TextView tvVolunteerName;
    @BindView(R.id.tvVolunteerAgeField)
    TextView tvVolunteerAge;
    @BindView(R.id.tvVolunteerGenderField)
    TextView tvVolunteerGender;
    @BindView(R.id.tvVolunteerEmailField)
    TextView tvVolunteerEmail;
    @BindView(R.id.rvActivities)
    RecyclerView rvActivities;

    private VolunteerActivityViewModel mVolunteerActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        ButterKnife.bind(this);

        mVolunteerEmail = getIntent().getStringExtra(EXTRA_VOLUNTEER_EMAIL);
        mProjectId = getIntent().getIntExtra(EXTRA_PROJECT_ID, -1);

        //Setting ViewModel and LiveData
        mVolunteerActivityViewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel.class);
        mVolunteerActivityViewModel.setVolunteerInfo(mProjectId, mVolunteerEmail);

        //Setting Volunteer RecyclerView
        final ActivityVolunteerAdapter activityVolunteerAdapter = new ActivityVolunteerAdapter(this, mVolunteerActivityViewModel);
        rvActivities.setAdapter(activityVolunteerAdapter);
        rvActivities.setLayoutManager(new LinearLayoutManager(this));

        mVolunteerActivityViewModel.getVolunteer().observe(this, new Observer<Volunteer>() {
            @Override
            public void onChanged(@Nullable Volunteer volunteer) {
                tvVolunteerName.setText(volunteer.getName());
                tvVolunteerEmail.setText(volunteer.getEmail());
                tvVolunteerGender.setText(DataUtils.convertGender(volunteer.getGender(), getApplicationContext()));
                tvVolunteerAge.setText("" + DataUtils.convertBirthdayToAge(volunteer.getBirthday()) + " " + getString(R.string.years));
            }
        });

        mVolunteerActivityViewModel.getAllActivities().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(@Nullable List<Activity> activities) {
                mActivityList = activities;
                activityVolunteerAdapter.setActivityList(activities);
            }
        });
    }
}
