package zimmermann.larissa.moveitcollector.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(tableName = "activity_table",
        foreignKeys = @ForeignKey(entity = Project.class, parentColumns = "project_id", childColumns = "project_id"),
        primaryKeys = {"project_id", "name"})
public class Activity {

    @ColumnInfo(name = "project_id")
    private int projectId;
    @NonNull
    private String name;

    public Activity(int projectId, @NonNull String name) {
        this.projectId = projectId;
        this.name = name;
    }

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
}
