package zimmermann.larissa.moveitcollector;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zimmermann.larissa.moveitcollector.services.Accelerometer;

public class CollectorActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "PROJECT_ID_COLLECTOR";
    public static final String VOLUNTEER_NAME = "VOLUNTEER_NAME_COLLECTOR";
    public static final String ACTIVITY_NAME = "ACTIVITY_NAME_COLLECTOR";
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 2;

    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.ivPlayButton)
    ImageView btPlay;
    @BindView(R.id.ivPauseButton)
    ImageView btPause;
    @BindView(R.id.ivStopButton)
    ImageView btStop;

    private Accelerometer mAccelerometer;
    private int mProjectId;
    private String mVolunteerName;
    private String mActivityName;
    private long mLastPause = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        ButterKnife.bind(this);

        //Recovery data
        mProjectId = getIntent().getIntExtra(PROJECT_ID, 0);
        mVolunteerName = getIntent().getStringExtra(VOLUNTEER_NAME);
        mActivityName = getIntent().getStringExtra(ACTIVITY_NAME);

        //Chronometer
        mLastPause = SystemClock.elapsedRealtime();
        chronometer.setBase(mLastPause);

        //Request Permission
        checkWriteExternalStoragePermission(this);
    }

    @OnClick(R.id.ivPlayButton)
    public void startChronometer() {
        showPauseButton();
        chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - mLastPause);
        chronometer.start();
        mAccelerometer.start();
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.chronometer_play), Snackbar.LENGTH_SHORT).show();
        //TODO START COLLECTING DATA FROM ACCELEROMETER AND GYROSCOPY
    }

    @OnClick(R.id.ivPauseButton)
    public void pauseChronometer() {
        hidePauseButton();
        mLastPause = SystemClock.elapsedRealtime();
        chronometer.stop();
        mAccelerometer.pause();
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.chronometer_paused), Snackbar.LENGTH_SHORT).show();
        //TODO PAUSE COLLECTING DATA FROM ACCELEROMETER AND GYROSCOPY
    }

    @OnClick(R.id.ivStopButton)
    public void stopChronometer() {
        hidePauseButton();
        mLastPause = SystemClock.elapsedRealtime();
        chronometer.setBase(mLastPause);
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.chronometer_stopped), Snackbar.LENGTH_SHORT).show();
        //TODO STOP COLLECTING DATA FROM ACCELEROMETER AND GYROSCOPY
        mAccelerometer.pause();
    }

    private void showPauseButton() {
        btPause.setVisibility(View.VISIBLE);
        btPlay.setVisibility(View.INVISIBLE);
    }

    private void hidePauseButton() {
        btPause.setVisibility(View.INVISIBLE);
        btPlay.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        btPause.setVisibility(View.GONE);
        btPlay.setVisibility(View.GONE);
        btStop.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAccelerometer.stop();
        chronometer.stop();
    }

    private void checkWriteExternalStoragePermission(Activity activity) {
        if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            //Accelerometer
            initAccelerometer();
            //Hide Pause button
            hidePauseButton();
        }
    }

    private void initAccelerometer() {
        mAccelerometer = new Accelerometer(this, mProjectId, mVolunteerName, mActivityName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Accelerometer
                    initAccelerometer();
                    //Hide Pause button
                    hidePauseButton();
                }
                else {
                    //Hide buttons
                    hideButtons();
                    Snackbar.make(findViewById(android.R.id.content),
                            getString(R.string.write_external_storage_permission_not_grant),
                            Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.GRAY)
                            .show();
                }
                return;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
