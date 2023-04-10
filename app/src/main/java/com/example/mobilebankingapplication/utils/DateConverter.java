package com.example.mobilebankingapplication.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static Date stringToDate(String value) {
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date value) {
        if(value == null) {
            return null;
        }
        return simpleDateFormat.format(value);
    }

    public static Timestamp stringToTimestamp(String value){
        try {
            Date date = simpleDateTimeFormat.parse(value);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String timestampToString(Timestamp value) {
        if(value == null) {
            return null;
        }
        return simpleDateTimeFormat.format(value);
    }


}
