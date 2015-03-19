package com.coolcoders.cmsc628assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        ViewHolder viewHolder;

        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.entry_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView)convertView.findViewById(R.id.textView_time);
            viewHolder.info = (TextView)convertView.findViewById(R.id.textView_info);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        EntryItem e = getItem(position);
        viewHolder.info.setText(e.strInfo);
        viewHolder.time.setText(e.strTimeFull);

        return convertView;
    }

    private static class ViewHolder {
        TextView time;
        TextView info;
    }
}