package zimmermann.larissa.moveitcollector;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zimmermann.larissa.moveitcollector.adapter.ActivityAdapter;
import zimmermann.larissa.moveitcollector.adapter.VolunteerAdapter;
import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.repository.ProjectActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.CsvGenerator;
import zimmermann.larissa.moveitcollector.utils.DataUtils;
import zimmermann.larissa.moveitcollector.utils.FileManager;
import zimmermann.larissa.moveitcollector.utils.Gender;
import zimmermann.larissa.moveitcollector.utils.PermissionUtils;

public class ProjectActivity extends AppCompatActivity {
    private static final String TAG = ProjectActivity.class.getName();
    public static final String EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID";
    private static final int NEW_VOLUNTEER_REQUEST_CODE = 2;
    private static final int RADIO_BUTTON_UNSELECTED = -1;

    private ProjectActivityViewModel mProjectActivityViewModel;
    private int mProjectId;
    private Project mProject;

    @BindView(R.id.tvProjectTitle)
    TextView tvProjectTitle;
    @BindView(R.id.add_activity_button)
    Button mAddActivityLabelButton;
    @BindView(R.id.activity_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.btAddVolunteer)
    Button mAddVolunteerButton;
    @BindView(R.id.rvVolunteer)
    RecyclerView mVolunteerRecyclerView;
    @BindView(R.id.btGenerateCSVFile)
    Button btGenerateCSVFile;
    @BindView(R.id.ivCSVFile)
    ImageView ivCSVFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);

        mProjectId = getIntent().getIntExtra(EXTRA_PROJECT_ID, -1);

        //Setting ViewModel and LiveData
        mProjectActivityViewModel = ViewModelProviders.of(this).get(ProjectActivityViewModel.class);
        mProjectActivityViewModel.setProjectId(mProjectId);

        //Setting Activity Label RecyclerView
        final ActivityAdapter activityAdapter = new ActivityAdapter(this, mProjectActivityViewModel);
        mRecyclerView.setAdapter(activityAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_2)));

        //Setting Volunteer RecyclerView
        final VolunteerAdapter volunteerAdapter = new VolunteerAdapter(this, mProjectActivityViewModel);
        mVolunteerRecyclerView.setAdapter(volunteerAdapter);
        mVolunteerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Getting Data
        mProjectActivityViewModel.getAllVolunteers(mProjectId).observe(this, new Observer<List<Volunteer>>() {
            @Override
            public void onChanged(@Nullable List<Volunteer> volunteers) {
                volunteerAdapter.setVolunteerList(volunteers);
            }
        });

        mProjectActivityViewModel.getAllActivities(mProjectId).observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(@Nullable List<Activity> activities) {
                activityAdapter.setActivityList(activities);
            }
        });

        mProjectActivityViewModel.getProject().observe(this, new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {
                mProject = project;
                tvProjectTitle.setText(project.getName());
                checkCSVFileVisibility();
            }
        });


        //Check if project was clicked
        mProjectActivityViewModel.getOpenVolunteerEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String volunteerEmail) {
                if(volunteerEmail != null) {
                    openVolunteerDetails(volunteerEmail);
                }
            }
        });
    }

    private void checkCSVFileVisibility() {
        if(mProject != null) {
            File file = CsvGenerator.generateCSVFileName(this, mProject);
            if(file.exists()) ivCSVFile.setVisibility(View.VISIBLE);
            else ivCSVFile.setVisibility(View.GONE);
        }
    }

    private void openVolunteerDetails(String volunteerEmail) {
        Intent intent = new Intent(this, VolunteerActivity.class);
        intent.putExtra(VolunteerActivity.EXTRA_VOLUNTEER_EMAIL, volunteerEmail);
        intent.putExtra(VolunteerActivity.EXTRA_PROJECT_ID, mProjectId);
        startActivity(intent);
    }

    @OnClick(R.id.ivCSVFile)
    public void shareCSVFile(){
        if(mProject != null) {
            File file = CsvGenerator.generateCSVFileName(this, mProject);
            if(file.exists()) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.share_csv_file), mProject.getName().toString()));
                intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_share_csv_file));
                intentShareFile.setType("*/*");
                startActivity(Intent.createChooser(intentShareFile, getString(R.string.share_file)));
            }
        }
    }

    @OnClick(R.id.btGenerateCSVFile)
    public void generateCSVFile() {
        PermissionUtils.checkWriteExternalStoragePermission(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.generate_csv_file))
                .setMessage(getString(R.string.are_you_sure_generate_csv_file))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //TODO GENERATE CSV FILE
                        CsvGenerator csvGenerator = new CsvGenerator(getApplicationContext(), mProjectActivityViewModel.getProject().getValue());
                        try {
                            csvGenerator.generateCSV();
                            ivCSVFile.setVisibility(View.VISIBLE);
                            Snackbar.make(findViewById(android.R.id.content), getString(R.string.csv_file_generated), Snackbar.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onClick: " + e.getMessage());
                            Snackbar.make(findViewById(android.R.id.content), getString(R.string.csv_file_was_not_generated), Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @OnClick(R.id.btAddVolunteer)
    public void addNewVolunteer() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_volunteer_custom_dialog, null);
        alert.setView(dialogView);

        final EditText etVolunteerName = (EditText) dialogView.findViewById(R.id.etVolunteerName);
        final EditText etVolunteerEmail = (EditText) dialogView.findViewById(R.id.etVolunteerEmail);
        final EditText etVolunteerBirthday = (EditText) dialogView.findViewById(R.id.etVolunteerBirthday);
        final Button btAddDate = (Button) dialogView.findViewById(R.id.btAddDate);
        final RadioGroup rgGender = (RadioGroup) dialogView.findViewById(R.id.rgGender);
        final RadioButton rbFemale = (RadioButton) dialogView.findViewById(R.id.rbFemale);
        final RadioButton rbMale = (RadioButton) dialogView.findViewById(R.id.rbMale);

        btAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String birthday = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                etVolunteerBirthday.setText(birthday);

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        alert.setPositiveButton(getString(R.string.save_new_volunteer), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String volunteerName = etVolunteerName.getText().toString();
                String volunteerEmail = etVolunteerEmail.getText().toString();
                String volunteerBirthday = !TextUtils.isEmpty(etVolunteerBirthday.getText().toString()) ? etVolunteerBirthday.getText().toString() : null;
                String gender = rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "Male" : "Female";

                if(TextUtils.isEmpty(etVolunteerName.getText()) ||
                        TextUtils.isEmpty(etVolunteerEmail.getText()) ||
                        TextUtils.isEmpty(etVolunteerBirthday.getText()) ||
                        rgGender.getCheckedRadioButtonId() == RADIO_BUTTON_UNSELECTED) {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.all_fields_are_required), Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                else {
                    Date birthdayDate = DataUtils.convertStringToDate(volunteerBirthday);
                    Volunteer volunteer = new Volunteer(mProjectId,
                            volunteerEmail,
                            volunteerName,
                            birthdayDate,
                            gender.equals(getString(R.string.male)) ? Gender.M : Gender.F);

                    mProjectActivityViewModel.insertVolunteer(volunteer);
                }


            }
        });

        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.volunteer_was_not_created), Snackbar.LENGTH_LONG).show();
            }
        });

        alert.show();
    }

    @OnClick(R.id.add_activity_button)
    public void addNewActivityLabel() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_activity_custom_dialog, null);
        alert.setView(dialogView);
        final EditText editText = (EditText) dialogView.findViewById(R.id.etActivityName);

        alert.setPositiveButton(getString(R.string.save_new_activity_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                if(!TextUtils.isEmpty(editText.getText())) {
                    String activityLabel = editText.getText().toString();
                    Activity newActivity = new Activity(mProjectId, activityLabel);
                    mProjectActivityViewModel.insertActivity(newActivity);
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.activity_was_created), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.activity_was_not_created), Snackbar.LENGTH_LONG).show();
            }
        });
        alert.show();
    }
}
