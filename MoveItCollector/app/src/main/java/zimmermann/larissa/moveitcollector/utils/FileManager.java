package zimmermann.larissa.moveitcollector.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;

import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.database.Volunteer;


public class FileManager {
    private static final String TAG = FileManager.class.getName();

    private File mFile;
    private FileOutputStream mFileOutputStream;
    private OutputStreamWriter mOutputStreamWriter;
    private BufferedWriter mBufferedWriter;
    private PrintWriter mPrintWriter;

    private String mActivityName;

    public FileManager(Context context, int projectId, String activityName, String volunteerName) {

        mActivityName = activityName;

        mFile = buildPath(context, projectId, volunteerName, activityName);

        if (!mFile.getParentFile().exists()) {
            mFile.getParentFile().mkdirs();
        }

        if (!mFile.exists()){
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            mFileOutputStream = new FileOutputStream(mFile, true);
            mOutputStreamWriter = new OutputStreamWriter(mFileOutputStream);
            mBufferedWriter = new BufferedWriter(mOutputStreamWriter);
            mPrintWriter = new PrintWriter(mBufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Accelerometer: " + e.getMessage());
        }
    }

    public void write(String text) {
        mPrintWriter.write(getTimeStamp() + "," + text + "," + mActivityName + "\n");
    }

    public void releaseResources() {
        mPrintWriter.close();
        try {
            mBufferedWriter.close();
            mOutputStreamWriter.close();
            mFileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "stop: " + e.getMessage());
        }
    }

    private String getTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public static File buildPath(Context context, int projectId, String volunteerName, String activityName) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + context.getString(R.string.app_name) + "/" + projectId;
        String filename = activityName.replace(" ", "") + "_" + volunteerName.replace(" ", "");
        return new File(path ,filename + ".txt");
    }

    public static File buildProjectPath(Context context, int projectId) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + context.getString(R.string.app_name) + "/" + projectId;
        return new File(path);
    }

    public static File buildResultPath(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + context.getString(R.string.app_name) + "/csv";
        return new File(path);
    }

    public static boolean deleteFilesFromVolunteer(Context context, final Volunteer volunteer) {
        int projectId = volunteer.getProjectId();
        final String volunteerName = volunteer.getName().replace(" ", "");
        File folder = buildProjectPath(context, projectId);

        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                String regex = ".*" + volunteerName + ".txt";
                return name.matches(regex );
            }
        } );

        if(files != null) {
            for(final File file : files ) {
                if(!file.delete()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean deleteFilesFromActivity(Context context, final Activity activity) {
        int projectId = activity.getProjectId();
        final String activityName = activity.getName().replace(" ", "");
        File folder = buildProjectPath(context, projectId);

        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                String regex = activityName + ".*";
                return name.matches(regex );
            }
        } );

        if(files != null) {
            for(final File file : files ) {
                if(!file.delete()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean deleteFilesFromProject(Context context, final Project project) {
        int projectId = project.getProjectId();
        final String projectName = project.getName().replace(" ", "");

        //Project Files
        File folder = buildProjectPath(context, projectId);
        File[] files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                if(!file.delete()) {
                    return false;
                }
            }
        }
        folder.delete();

        //CSV Files
        folder = buildResultPath(context);
        files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                String regex = projectName + ".*";
                return name.matches(regex );
            }
        } );

        if(files != null) {
            for(final File file : files ) {
                if(!file.delete()) {
                    return false;
                }
            }
        }

        return true;
    }
}
