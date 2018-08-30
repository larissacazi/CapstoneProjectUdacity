package zimmermann.larissa.moveitcollector.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import zimmermann.larissa.moveitcollector.CollectorActivity;
import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.repository.VolunteerActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.FileManager;

public class ActivityVolunteerAdapter extends RecyclerView.Adapter<ActivityVolunteerAdapter.ActivityViewHolder>{

    private List<Activity> mActivityList;
    private Context mContext;
    private VolunteerActivityViewModel mVolunteerActivityViewModel;

    public ActivityVolunteerAdapter(Context context, VolunteerActivityViewModel volunteerActivityViewModel) {
        mContext = context;
        mVolunteerActivityViewModel = volunteerActivityViewModel;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item, parent, false);
        return new ActivityVolunteerAdapter.ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.activityName.setText(mActivityList.get(position).getName());

        int projectId = mVolunteerActivityViewModel.getVolunteer().getValue().getProjectId();
        String volunteerName = mVolunteerActivityViewModel.getVolunteer().getValue().getName();
        String activityName = mActivityList.get(position).getName();
        File mFile = FileManager.buildPath(mContext, projectId, volunteerName, activityName);

        if(mFile.exists()) holder.ivFolder.setImageDrawable(mContext.getDrawable(R.drawable.ic_folder_48dp));
        else holder.ivFolder.setImageDrawable(mContext.getDrawable(R.drawable.ic_folder_open_48dp));
    }

    @Override
    public int getItemCount() {
        return mActivityList == null ? 0 : mActivityList.size();
    }

    public void setActivityList(List<Activity> activityList) {
        mActivityList = activityList;
        notifyDataSetChanged();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        TextView activityName;
        ImageView ivFolder;
        Button btCollectOrSee;


        public ActivityViewHolder(View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.tvActivity);
            ivFolder = itemView.findViewById(R.id.ivFolder);
            btCollectOrSee = itemView.findViewById(R.id.btCollectOrSee);

            btCollectOrSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(mContext, CollectorActivity.class);
                    intent.putExtra(CollectorActivity.PROJECT_ID, mVolunteerActivityViewModel.getVolunteer().getValue().getProjectId());
                    intent.putExtra(CollectorActivity.VOLUNTEER_NAME, mVolunteerActivityViewModel.getVolunteer().getValue().getName());
                    intent.putExtra(CollectorActivity.ACTIVITY_NAME, mActivityList.get(position).getName());
                    mContext.startActivity(intent);

                    ivFolder.setImageDrawable(mContext.getDrawable(R.drawable.ic_folder_48dp));
                }
            });

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            int position = getAdapterPosition();
            final Activity activity = mActivityList.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext())
                    .setTitle(v.getResources().getString(R.string.delete_data_collected))
                    .setMessage(v.getResources().getString(R.string.are_yousure_delete_data_collected))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //Remove data from activity
                            File file = FileManager.buildPath(mContext,
                                    activity.getProjectId(),
                                    mVolunteerActivityViewModel.getVolunteer().getValue().getName(),
                                    activity.getName());

                            if(file.delete()) {
                                ivFolder.setImageDrawable(mContext.getDrawable(R.drawable.ic_folder_open_48dp));
                                Snackbar.make(v, activity.getName() + ": " + mContext.getString(R.string.data_from_activity_removed), Snackbar.LENGTH_LONG).show();
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
    }
}