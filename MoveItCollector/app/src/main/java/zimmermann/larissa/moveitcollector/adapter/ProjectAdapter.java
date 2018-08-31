package zimmermann.larissa.moveitcollector.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zimmermann.larissa.moveitcollector.MainActivity;
import zimmermann.larissa.moveitcollector.ProjectActivity;
import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Project;
import zimmermann.larissa.moveitcollector.repository.MainActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.FileManager;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    private List<Project> mProjectList;
    private Context mContext;
    private Activity mActivity;
    private MainActivityViewModel mMainActivityViewModel;

    public ProjectAdapter(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.project_list_item, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.mProjectTitle.setText(mProjectList.get(position).getName());
        holder.mProjectStartDate.setText(mContext.getString(R.string.created_word) + mProjectList.get(position).getStartDate().toString());
    }

    @Override
    public int getItemCount() {
        return mProjectList != null ? mProjectList.size() : 0;
    }

    public void setProjectList(List<Project> projectList) {
        mProjectList = projectList;
        notifyDataSetChanged();
    }

    public void setMainActivityViewModel(MainActivityViewModel mainActivityViewModel) {
        mMainActivityViewModel = mainActivityViewModel;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        TextView mProjectTitle;
        TextView mProjectStartDate;
        Button mShowProjectButton;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            mProjectTitle = itemView.findViewById(R.id.project_name);
            mProjectStartDate = itemView.findViewById(R.id.project_start_date);
            mShowProjectButton = itemView.findViewById(R.id.project_details_button);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            mShowProjectButton.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            int position = getAdapterPosition();
            final Project project = mProjectList.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext())
                    .setTitle(v.getResources().getString(R.string.delete_project))
                    .setMessage(v.getResources().getString(R.string.are_you_sure_delete_project))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //Remove all files
                            if(!FileManager.deleteFilesFromProject(mContext, project)) {
                                Snackbar.make(v, mContext.getString(R.string.project_was_not_removed), Snackbar.LENGTH_LONG).show();
                            }
                            else {
                                //Remove project
                                mMainActivityViewModel.delete(project);
                                Snackbar.make(v, mContext.getString(R.string.project_was_removed), Snackbar.LENGTH_LONG).show();
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

            return true;
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            View view = v.findViewById(R.id.project_name);
            final Project project = mProjectList.get(position);

            if(view == null) {
                view = v.getRootView().findViewById(R.id.project_name);
            }

            ActivityOptionsCompat activityOptionsCompat
                    = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    view, view.getTransitionName());
            Intent intent = new Intent(mContext, ProjectActivity.class);
            intent.putExtra(ProjectActivity.EXTRA_PROJECT_ID, project.getProjectId());
            mContext.startActivity(intent, activityOptionsCompat.toBundle());

        }
    }
}
