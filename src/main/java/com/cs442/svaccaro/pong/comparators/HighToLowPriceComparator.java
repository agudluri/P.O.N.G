package com.cs442.svaccaro.pong.comparators;

import com.cs442.svaccaro.pong.Game;

import java.util.Comparator;

/**
 * Created by harsha on 11/25/2017.
 */

public class HighToLowPriceComparator implements Comparator<Game> {
    @Override
    public int compare(Game a, Game b) {
        return a.getHighPrice() > b.getHighPrice() ? -1 : a.getHighPrice() == b.getHighPrice() ? 0 : 1;
    }
}