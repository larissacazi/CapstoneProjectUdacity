package zimmermann.larissa.moveitcollector.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import zimmermann.larissa.moveitcollector.utils.FileManager;


public class Accelerometer implements SensorEventListener {

    private static final String TAG = Accelerometer.class.getName();
    private static final float ALPHA = (float)0.8;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private final float[] gravity = new float[3];
    private final float[] linear_acceleration = new float[3];
    private float acceleration;

    private FileManager mFileManager;

    public Accelerometer(Context context, int projectId, String volunteerName, String activityName) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mFileManager = new FileManager(context, projectId, activityName, volunteerName);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        acceleration = (float) Math.sqrt(Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2) + Math.pow(linear_acceleration[2], 2));

        /*Log.i(TAG, "Ax:" + Float.toString(linear_acceleration[0]) +
                "\nAy:" + Float.toString(linear_acceleration[1]) +
                "\nAz:" + Float.toString(linear_acceleration[2]) +
                "\nAcceleration:" + Float.toString(acceleration));*/

        //Save on file
        save(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2], acceleration);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void pause() {
        sensorManager.unregisterListener(this);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
        mFileManager.releaseResources();
    }

    private void save(float ax, float ay, float az, float acceleration) {
        mFileManager.write(ax + "," + ay + "," + az + "," + acceleration);
    }
}
