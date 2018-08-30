package zimmermann.larissa.moveitcollector.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Activity;
import zimmermann.larissa.moveitcollector.repository.ProjectActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.FileManager;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>{

    private List<Activity> mActivityList;
    private Context mContext;
    private ProjectActivityViewModel mProjectActivityViewModel;

    public ActivityAdapter(Context context, ProjectActivityViewModel projectActivityViewModel) {
        mContext = context;
        mProjectActivityViewModel = projectActivityViewModel;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.label_list_item, parent, false);
        return new ActivityAdapter.ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.activityName.setText(mActivityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mActivityList == null ? 0 : mActivityList.size();
    }

    public void setActivityList(List<Activity> activityList) {
        mActivityList = activityList;
        notifyDataSetChanged();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView activityName;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            activityName = itemView.findViewById(R.id.activity_label_name);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            int position = getAdapterPosition();
            final Activity activity = mActivityList.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext())
                    .setTitle(v.getResources().getString(R.string.delete_activity))
                    .setMessage(v.getResources().getString(R.string.are_you_sure_delete_activity))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //Remove all files
                            //Delete files
                            if(!FileManager.deleteFilesFromActivity(mContext, activity)) {
                                Snackbar.make(v, mContext.getString(R.string.activity_was_not_removed), Snackbar.LENGTH_LONG).show();
                            }
                            else {
                                //Remove activity
                                mProjectActivityViewModel.deleteActivity(activity);
                                Snackbar.make(v, mContext.getString(R.string.activity_was_removed), Snackbar.LENGTH_LONG).show();
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
