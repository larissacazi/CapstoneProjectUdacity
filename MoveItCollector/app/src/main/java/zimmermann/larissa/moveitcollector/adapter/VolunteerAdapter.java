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

import java.io.File;
import java.util.List;

import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Volunteer;
import zimmermann.larissa.moveitcollector.repository.ProjectActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.FileManager;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder>{

    private List<Volunteer> mVolunteerList;
    private Context mContext;
    private ProjectActivityViewModel mProjectActivityViewModel;

    public VolunteerAdapter(Context context, ProjectActivityViewModel projectActivityViewModel) {
        mContext = context;
        mProjectActivityViewModel = projectActivityViewModel;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.volunteer_list_item, parent, false);
        return new VolunteerAdapter.VolunteerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        holder.volunteerName.setText(mVolunteerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mVolunteerList == null ? 0 : mVolunteerList.size();
    }

    public void setVolunteerList(List<Volunteer> volunteerList) {
        mVolunteerList = volunteerList;
        notifyDataSetChanged();
    }

    public class VolunteerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView volunteerName;

        public VolunteerViewHolder(View itemView) {
            super(itemView);

            volunteerName = itemView.findViewById(R.id.tvVolunteer);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            int position = getAdapterPosition();
            final Volunteer volunteer = mVolunteerList.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext())
                    .setTitle(v.getResources().getString(R.string.delete_volunteer))
                    .setMessage(v.getResources().getString(R.string.are_you_sure_delete_volunteer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            //Delete files
                            if(!FileManager.deleteFilesFromVolunteer(mContext, volunteer)) {
                                Snackbar.make(v, mContext.getString(R.string.volunteer_was_not_removed), Snackbar.LENGTH_LONG).show();
                            }
                            else {
                                //Remove volunteer
                                mProjectActivityViewModel.deleteVolunteer(volunteer);
                                Snackbar.make(v, mContext.getString(R.string.volunteer_removed), Snackbar.LENGTH_LONG).show();
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
            final Volunteer volunteer = mVolunteerList.get(position);
            mProjectActivityViewModel.getOpenVolunteerEvent().setValue(volunteer.getEmail());
        }
    }
}
