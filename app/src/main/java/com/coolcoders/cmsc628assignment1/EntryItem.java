package com.coolcoders.cmsc628assignment1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryItem {

    public final long lTimeStart, lTimeEnd; // will be displayed as strings
    public final String strTimeStart, strTimeEnd, strTimeFull; // reprsents time strings
    public final String strInfo; // label of the user's activity

    public EntryItem(String info, long lTimeStart, long lTimeEnd) {

        // Reads in time values as milliseconds
        this.strInfo = info;
        this.lTimeStart = lTimeStart;
        this.lTimeEnd = lTimeEnd;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        strTimeStart = sdf.format(new Date(this.lTimeStart));
        strTimeEnd = sdf.format(new Date(this.lTimeEnd));
        strTimeFull = strTimeStart + "-" + strTimeEnd;
    }
}
