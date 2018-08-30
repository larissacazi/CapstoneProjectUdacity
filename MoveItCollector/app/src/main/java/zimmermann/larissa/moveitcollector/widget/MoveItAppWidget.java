package zimmermann.larissa.moveitcollector.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.List;

import zimmermann.larissa.moveitcollector.MainActivity;
import zimmermann.larissa.moveitcollector.ProjectActivity;
import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Project;

/**
 * Implementation of App Widget functionality.
 */
public class MoveItAppWidget extends AppWidgetProvider {

    public static final String WIDGET_BUTTON_ACTION = "zimmermann.moveitcollector.widget.WIDGET_BUTTON_ACTION";
    public static List<Project> projectList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], List<Project> projects) {
        projectList = projects;

        for(int appWidgetId : appWidgetIds) {
            //Setting Adapter
            Intent intent = new Intent(context, ListViewWidgetService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.move_it_app_widget);
            views.setRemoteAdapter(R.id.projects_list_view, intent);
            ComponentName component = new ComponentName(context, MoveItAppWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.projects_list_view);

           //Starting Activity
            Intent startActivityIntent = new Intent(context, ProjectActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.projects_list_view, startActivityPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if(intent.getAction().equals(MoveItAppWidget.WIDGET_BUTTON_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int projectId = intent.getIntExtra(ProjectActivity.EXTRA_PROJECT_ID, -1);
            if(projectId >= 0) {
                Intent i = new Intent(context, ProjectActivity.class);
                intent.putExtra(ProjectActivity.EXTRA_PROJECT_ID, projectId);
                context.startActivity(i);
            }
        }

        super.onReceive(context, intent);
    }
}

