package com.coolcoders.cmsc628assignment1;

public class EntryItem {

    public final int iTimeStart, iTimeEnd; // will be displayed as strings
    public final String strTime, strInfo; // these strings will be displayed

    public EntryItem(String info, int iTimeStart, int iTimeEnd) {

        // TODO: Make sure time integers are displayed properly as times

        this.strInfo = info;
        this.iTimeStart = iTimeStart;
        this.iTimeEnd = iTimeEnd;
        strTime = (new Integer(iTimeStart)).toString() + " - " +
                (new Integer(iTimeEnd)).toString();
    }
}
