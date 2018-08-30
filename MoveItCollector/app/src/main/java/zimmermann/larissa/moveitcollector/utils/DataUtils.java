package zimmermann.larissa.moveitcollector.utils;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import zimmermann.larissa.moveitcollector.R;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DataUtils {
    private static final String TAG = DataUtils.class.getName();

    public static int convertBirthdayToAge(Date birthday) {
        Calendar birth = Calendar.getInstance(Locale.US);
        birth.setTime(birthday);
        Calendar now = Calendar.getInstance(Locale.US);
        int age = now.get(YEAR) - birth.get(YEAR);
        if(birth.get(MONTH) > now.get(MONTH) || (birth.get(MONTH) == now.get(MONTH) && birth.get(DATE) > now.get(DATE))) {
            age--;
        }
        return age;
    }

    public static Date convertStringToDate(String birthday) {
        Calendar cal = Calendar.getInstance();
        String[] date = birthday.split("/");

        Date birthdayDate = null;
        if(date != null && date.length == 3) {
            cal.set(Calendar.YEAR, Integer.valueOf(date[2]));
            cal.set(Calendar.MONTH, Integer.valueOf(date[1]));
            cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[0]));
            birthdayDate = cal.getTime();
        }

        return birthdayDate;
    }

    public static String convertDateToString(Date date) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");

        try{
            dateString = sdfr.format( date );
        }catch (Exception ex ){
            Log.i(TAG, "convertDateToString: ERROR :: " + ex.getMessage());
        }

        return dateString;
    }

    public static String convertGender(Gender g, Context context) {
        switch (g) {
            case F: return context.getString(R.string.female);
            case M: return context.getString(R.string.male);
        }
        return context.getString(R.string.unknown);
    }
}
