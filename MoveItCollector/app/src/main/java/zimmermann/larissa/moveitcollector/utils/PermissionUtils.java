package zimmermann.larissa.moveitcollector.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static zimmermann.larissa.moveitcollector.CollectorActivity.WRITE_EXTERNAL_STORAGE_PERMISSION;

public class PermissionUtils {
    public static void checkWriteExternalStoragePermission(Activity activity) {
        if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }
}
