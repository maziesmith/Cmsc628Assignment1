package com.coolcoders.cmsc628assignment1;

import android.graphics.drawable.Drawable;

import java.security.Timestamp;

/**
 * Created by Michael on 3/12/2015.
 */
public class ListItem {

    public final String info;       // text appears in the list item
    public final int iTimeStart, iTimeEnd; // will be displayed as strings
    public final String sTimeString;

    public ListItem(String info, int iTimeStart, int iTimeEnd) {

        // TODO: Make sure time integers are displayed properly as times

        this.info = info;
        this.iTimeStart = iTimeStart;
        this.iTimeEnd = iTimeEnd;
        sTimeString = (new Integer(iTimeStart)).toString() + " - " +
                (new Integer(iTimeEnd)).toString();
    }


}
