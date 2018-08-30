package zimmermann.larissa.moveitcollector.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;

import zimmermann.larissa.moveitcollector.utils.Gender;


@Entity(tableName = "volunteer_table",
        foreignKeys = @ForeignKey(entity = Project.class, parentColumns = "project_id", childColumns = "project_id"),
        primaryKeys = {"project_id", "email"})
public class Volunteer {

    @ColumnInfo(name = "project_id")
    private int projectId;
    @NonNull
    private String email;
    @NonNull
    private String name;
    private Date birthday;
    private Gender gender;

    public Volunteer(int projectId, @NonNull String email, @NonNull String name, Date birthday, Gender gender) {
        this.projectId = projectId;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
