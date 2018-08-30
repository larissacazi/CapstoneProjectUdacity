package zimmermann.larissa.moveitcollector.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "project_table")
public class Project implements Parcelable {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "project_id")
    private int projectId;
    @NonNull
    private String name;
    @ColumnInfo(name = "start_date")
    private Date startDate;
    @ColumnInfo(name = "researcher_email")
    @NonNull
    private String researcherEmail;


    public Project(@NonNull String name, Date startDate, @NonNull String researcherEmail) {
        this.name = name;
        this.startDate = startDate;
        this.researcherEmail = researcherEmail;
    }

    protected Project(Parcel in) {
        projectId = in.readInt();
        name = in.readString();
        researcherEmail = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @NonNull
    public String getResearcherEmail() {
        return researcherEmail;
    }

    public void setResearcherEmail(@NonNull String researcherEmail) {
        this.researcherEmail = researcherEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(projectId);
        dest.writeString(name);
        dest.writeString(researcherEmail);
    }
}
