package com.coolcoders.cmsc628assignment1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents an entry on a list of activities (sitting, walking, lying down).
 */
public class EntryItem {
    /** The time when this activity started. Will be displayed as a string. */
    public final long lTimeStart;
    /** The time when this activity ended. Will be displayed as a string. */
    public final long lTimeEnd;
    /** The String form of lTimeStart, the time when the activity started. */
    public final String strTimeStart;
    /** The String form of lTimeEnd, the time when the activity ended. */
    public final String strTimeEnd;
    /** The time duration of the activity, as a String. */
    public final String strTimeFull;
    /** The label for this activity, such as "Sitting", "Walking", or "Lying Down". */
    public final String strInfo;

    /**
     * Constructs a new entry for a list of the user's activities.
     * @param info a String description of the user's activity, such as "Sitting"
     * @param lTimeStart when the activity started
     * @param lTimeEnd when the activity ended
     */
    public EntryItem(String info, long lTimeStart, long lTimeEnd) {
        // Reads in time values as milliseconds
        this.strInfo = info;
        this.lTimeStart = lTimeStart;
        this.lTimeEnd = lTimeEnd;
        // Format the times and store them in Strings.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        strTimeStart = sdf.format(new Date(this.lTimeStart));
        strTimeEnd = sdf.format(new Date(this.lTimeEnd));
        strTimeFull = strTimeStart + " - " + strTimeEnd;
    }

    /* Call this when writing the list of entries to an XML file*/
    public static String EntryListString(List<EntryItem> list) {
        String strout = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data>";
        for (int i = 0; i < list.size(); i++) {
            strout += "<entry><time>" +
                    list.get(i).strTimeFull + "</time><info>" +
                    list.get(i).strInfo + "</info></entry>";
        }
        strout += "</data>";
        return strout;
    }
}
