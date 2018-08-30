package zimmermann.larissa.moveitcollector;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zimmermann.larissa.moveitcollector.adapter.ProjectAdapter;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.repository.MainActivityViewModel;
import zimmermann.larissa.moveitcollector.widget.WidgetUpdateService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NEW_PROJECT_ACTIVITY_REQUEST_CODE = 1;
    public static final String RESEARCHER_INFO_FILE = "RESEARCHER_INFO_FILE";
    public static final String RESEARCHER_EMAIL = "RESEARCHER_EMAIL";
    public static final String RESEARCHER_PROFILE_URL = "RESEARCHER_PROFILE_URL";

    @BindView(R.id.project_recycler_view)
    RecyclerView mProjectRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.rivProfile)
    RoundedImageView rivProfile;

    private MainActivityViewModel mMainActivityViewModel;
    private int projectId;
    private String researcherEmail;
    private String researcherURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        supportPostponeEnterTransition();

        //Setting Profile Image
        settingResearcherProfileImage();


        //Setting RecyclerView
        final ProjectAdapter projectAdapter = new ProjectAdapter(this, this);
        mProjectRecyclerView.setAdapter(projectAdapter);
        mProjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Setting ViewModel and LivaData
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                projectAdapter.setProjectList(projects);
                startWidgetService(projects);
            }
        });
        projectAdapter.setMainActivityViewModel(mMainActivityViewModel);

        //Check if project was clicked
        mMainActivityViewModel.getOpenProjectEvent().observe(this, new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {
                if(project != null) {
                    openProjectDetails(project);
                }
            }
        });

        projectAdapter.setMainActivityViewModel(mMainActivityViewModel);

        //Setting FAB
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProject();
            }
        });
    }

    private void settingResearcherProfileImage() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.RESEARCHER_INFO_FILE, Context.MODE_PRIVATE);
        researcherEmail = sharedPref.getString(RESEARCHER_EMAIL, "");
        researcherURL = sharedPref.getString(RESEARCHER_PROFILE_URL, "");

        if(researcherURL != null && !researcherURL.isEmpty()) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(getResources().getColor(R.color.colorAccent))
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();

            Picasso.get().load(researcherURL)
                    .fit()
                    .transform(transformation)
                    .placeholder(getDrawable(R.drawable.ic_person_accent_48dp))
                    .into(rivProfile, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError(Exception e) {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }
    }

    private void addProject() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_project_custom_dialog, null);
        alert.setView(dialogView);
        final EditText editText = (EditText) dialogView.findViewById(R.id.etProjectName);

        alert.setPositiveButton(getString(R.string.add_new_project), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                if(!TextUtils.isEmpty(editText.getText())) {
                    String projectName = editText.getText().toString();
                    Project project = new Project(projectName, new GregorianCalendar().getTime(), researcherEmail);
                    mMainActivityViewModel.insert(project);
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.project_was_created), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.project_was_not_created), Snackbar.LENGTH_LONG).show();
            }
        });

        alert.show();
    }

    private void openProjectDetails(Project project) {
        View view = findViewById(R.id.project_name);

        ActivityOptionsCompat activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view,
                        view.getTransitionName());

        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra(ProjectActivity.EXTRA_PROJECT_ID, project.getProjectId());

        startActivity(intent, activityOptionsCompat.toBundle());
    }

    private void startWidgetService(List<Project> projects) {
        Intent i = new Intent(this, WidgetUpdateService.class);
        i.setAction(WidgetUpdateService.WIDGET_UPDATE_ACTION);
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(WidgetUpdateService.PROJECTS_LIST, (Parcelable[]) projects.toArray(new Project[projects.size()]));
        i.putExtra(WidgetUpdateService.PROJECT_LIST_EXTRA, bundle);
        i.setAction(WidgetUpdateService.WIDGET_UPDATE_ACTION);
        startService(i);
    }

}
