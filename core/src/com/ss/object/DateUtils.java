package com.ss.object;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static int dayDiff(long t1, long t2) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(t1);
        int d1 = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTimeInMillis(t2);
        int d2 = cal.get(Calendar.DAY_OF_YEAR);
        return Math.abs(d2 - d1);
    }

    public static int dayDiff(int t1, int t2) {
        return dayDiff(((long)t1)*1000, ((long)t2)*1000);
    }

    public static int weekDiff(long t1, long t2) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(t1);
        int w1 = cal.get(Calendar.WEEK_OF_YEAR);
        cal.setTimeInMillis(t2);
        int w2 = cal.get(Calendar.WEEK_OF_YEAR);
        return Math.abs(w2 - w1);
    }


    public static int weekDiff(int t1, int t2) {
        return weekDiff(((long)t1)*1000, ((long)t2)*1000);
    }

    public static long getMillisFromDateString(String dateString, String pattern) throws Exception
    {
        Date date = new SimpleDateFormat(pattern).parse(dateString);
        return date.getTime();
    }
    public static int hourDiff(long t1, long t2) {
        int dd = dayDiff(t1, t2);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(t1);
        int h1 = cal.get(Calendar.HOUR_OF_DAY);
        cal.setTimeInMillis(t2);
        int h2 = cal.get(Calendar.HOUR_OF_DAY);
        return dd*24 + (h2 - h1);
    }

    public static void main(String args[]) throws Exception {
        String date1 = "2020-01-15 00:55:55";
        String date2 = "2020-02-15 01:55:55";

        long d1 = getMillisFromDateString(date1, DATE_PATTERN);
        long d2 = getMillisFromDateString(date2, DATE_PATTERN);

        int dayDiff = hourDiff(d1, d2);
        System.out.println(dayDiff);

    }
}
