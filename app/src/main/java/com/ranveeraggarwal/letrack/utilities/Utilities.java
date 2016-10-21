package com.ranveeraggarwal.letrack.utilities;

import java.util.Calendar;

/**
 * Created by raagga on 21-10-2016.
 */

public class Utilities {

    public static long getCurrentMonth() {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.AM_PM);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }

    public static long getCurrentMonthOffset(int offset) {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.AM_PM);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, offset);
        return cal.getTimeInMillis();
    }

    public static long getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.AM_PM);
        return cal.getTimeInMillis();
    }
}
