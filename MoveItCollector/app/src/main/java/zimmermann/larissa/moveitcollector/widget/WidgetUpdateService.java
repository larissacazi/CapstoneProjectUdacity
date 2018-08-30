package zimmermann.larissa.moveitcollector.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import zimmermann.larissa.moveitcollector.database.Project;

public class WidgetUpdateService extends IntentService {
    public static final String PROJECTS_LIST = "PROJECTS_LIST";
    public static final String PROJECT_LIST_EXTRA = "PROJECT_LIST_WIDGET_EXTRA";
    public static final String WIDGET_UPDATE_ACTION = "zimmermann.larissa.moveitcollector.update_widget";
    private List<Project> mProjectList;

    public WidgetUpdateService() {
        super(WidgetUpdateService.class.getName());
        mProjectList = new ArrayList<Project>();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(WIDGET_UPDATE_ACTION)){
            //Get Project List
            Bundle bundle = intent.getBundleExtra(PROJECT_LIST_EXTRA);
            Parcelable[] parcelables = bundle.getParcelableArray(PROJECTS_LIST);
            if(parcelables != null) {
                for (int i = 0; i < parcelables.length; i++) {
                    mProjectList.add((Project) parcelables[i]);
                }
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MoveItAppWidget.class));
            MoveItAppWidget.updateAppWidget(this, appWidgetManager, appWidgetIds, mProjectList);
        }
    }
}
