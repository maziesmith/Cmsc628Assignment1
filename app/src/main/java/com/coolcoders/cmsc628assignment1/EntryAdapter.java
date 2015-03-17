package com.coolcoders.cmsc628assignment1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

/**
 * Use this to manage the list of EntryItem object instances
 */
public class EntryAdapter extends ArrayAdapter<EntryItem> {
    public EntryAdapter(Context context, List<EntryItem> items) {
        super(context, R.layout.entry_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EntryItem e = getItem(position);

        if (convertView==null) {

        }

        return convertView;
    }
}