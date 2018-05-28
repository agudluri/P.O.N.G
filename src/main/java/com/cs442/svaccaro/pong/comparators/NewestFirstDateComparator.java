package com.cs442.svaccaro.pong.comparators;

import com.cs442.svaccaro.pong.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by harsha on 11/25/2017.
 */

public class NewestFirstDateComparator implements Comparator<Result> {
    @Override
    public int compare(Result a, Result b) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        Date date1 = null;
        try {
            date1 = format.parse(a.getReleasedate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date2 = null;
        try {
            date2 = format.parse(b.getReleasedate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2.compareTo(date1);
    }
}