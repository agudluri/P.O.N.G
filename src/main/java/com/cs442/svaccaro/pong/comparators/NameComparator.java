package com.cs442.svaccaro.pong.comparators;

import com.cs442.svaccaro.pong.Result;

import java.util.Comparator;

/**
 * Created by harsha on 11/25/2017.
 */

public class NameComparator implements Comparator<Result> {
    @Override
    public int compare(Result a, Result b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}