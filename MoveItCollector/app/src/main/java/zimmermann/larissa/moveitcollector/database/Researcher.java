package zimmermann.larissa.moveitcollector.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import zimmermann.larissa.moveitcollector.utils.Gender;


@Entity(tableName = "researcher_table")
public class Researcher {

    @NonNull
    private String name;
    private Date birthday;
    @PrimaryKey
    @NonNull
    private String email;
    @ColumnInfo(name = "academic_institute")
    @NonNull
    private String academicInstitute;
    private Gender gender;
    private String photoUrl;

    public Researcher(@NonNull String name, Date birthday, @NonNull String email, @NonNull String academicInstitute, Gender gender, String photoUrl) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.academicInstitute = academicInstitute;
        this.gender = gender;
        this.photoUrl = photoUrl;
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

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getAcademicInstitute() {
        return academicInstitute;
    }

    public void setAcademicInstitute(@NonNull String academicInstitute) {
        this.academicInstitute = academicInstitute;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
