package zimmermann.larissa.moveitcollector.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Project;

import static zimmermann.larissa.moveitcollector.ProjectActivity.EXTRA_PROJECT_ID;

public class ListViewWidgetService extends RemoteViewsService {

    public static final String POSITION_WIDGET = "POSITION_WIDGET";
    public static final String PROJECT_ID_WIDGET = "PROJECT_ID_WIDGET";

    public ListViewWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsFactory(getApplicationContext(), intent);
    }

    class ListViewsFactory implements RemoteViewsFactory {

        private Context context;
        private List<Project> mProjectList;
        private int mAppWidgetId;

        public ListViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        //Called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            this.mProjectList = MoveItAppWidget.projectList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mProjectList == null ? 0 : mProjectList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.project_list_item_widget);
            int projectId = mProjectList.get(position).getProjectId();
            String projectName = mProjectList.get(position).getName();
            views.setTextViewText(R.id.project_name_widget, projectName);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(EXTRA_PROJECT_ID, projectId);
            fillInIntent.setAction(MoveItAppWidget.WIDGET_BUTTON_ACTION);

            views.setOnClickFillInIntent(R.id.project_details_button_widget, fillInIntent);

            return views;
        }



        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }
}
