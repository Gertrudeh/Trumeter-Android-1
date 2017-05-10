package com.iconasystems.android.trumeter.utils;

/**
 * Created by MUSHABE on 6/17/2016.
 */
public class DateUtils {
    public static String parseDayToString(int day){
        String dayString = null;
        switch (day){
            case 1:
                dayString = "Mon";
                break;
            case 2:
                dayString = "Tue";
                break;
            case 3:
                dayString = "Wed";
                break;
            case 4:
                dayString = "Thur";
                break;
            case 5:
                dayString = "Fri";
                break;
            case 6:
                dayString = "Sat";
                break;
            case 7:
                dayString = "Sun";
        }

        return dayString;
    }

    public static String parseMonthToString(int month){
        String monthString = null;
        switch (month){
            case 1:
                monthString = "JAN";
                break;
            case 2:
                monthString = "FEB";
                break;
            case 3:
                monthString = "MAR";
                break;
            case 4:
                monthString = "APR";
                break;
            case 5:
                monthString = "MAY";
                break;
            case 6:
                monthString = "JUN";
                break;
            case 7:
                monthString = "JUL";
                break;
            case 8:
                monthString = "AUG";
                break;
            case 9:
                monthString = "SEP";
                break;
            case 10:
                monthString = "OCT";
                break;
            case 11:
                monthString = "NOV";
                break;
            case 12:
                monthString = "DEC";
                break;
        }
        return monthString;
    }

    public static String dateTimeBuilder(String dayOfWeek,int day, String month, int hour, int minutes){
        StringBuilder dateTimeBuilder = new StringBuilder()
                .append(dayOfWeek).append(" ")
                .append(" ")
                .append(day)
                .append(" ")
                .append(month)
                .append(" ")
                .append(hour)
                .append(":")
                .append(minutes)
                .append(" hrs");
        return dateTimeBuilder.toString();
    }

    public static String dateBuilder(String dayOfWeek,int day, String month, int year){
        StringBuilder dateTimeBuilder = new StringBuilder()
                .append(dayOfWeek).append(" ")
                .append(" ")
                .append(day)
                .append(" ")
                .append(month)
                .append(" ")
                .append(year);
        return dateTimeBuilder.toString();
    }
}
