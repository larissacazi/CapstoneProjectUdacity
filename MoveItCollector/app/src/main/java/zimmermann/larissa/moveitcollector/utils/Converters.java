package zimmermann.larissa.moveitcollector.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Gender fromGenderId(Integer genderId) {
        return genderId == null ? null : Gender.values()[genderId];
    }

    @TypeConverter
    public Integer fromGenderEnum(Gender gender) {
        return gender.ordinal();
    }
}
