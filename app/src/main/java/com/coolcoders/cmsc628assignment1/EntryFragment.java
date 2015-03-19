package com.coolcoders.cmsc628assignment1;

import android.app.ListFragment;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * List UI that contains entries
 */
public class EntryFragment extends ListFragment {

    private static List<EntryItem> _items;

    // TODO: populate list from MainActivity
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _items = new ArrayList<EntryItem>();
        //Resources resources = getResources();

        _items.add(new EntryItem("actual time",System.currentTimeMillis(),Calendar.getInstance().get(Calendar.SECOND)));
        _items.add(new EntryItem("one", 1, 10));
        _items.add(new EntryItem("tenteen", 191, 200));

        // send data to the adapter for displaying
        setListAdapter(new EntryAdapter(getActivity(), _items));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        int[] colors = { 0xA8A8A8A8, 0xFFFFFFFF, 0xA8A8A8A8 };
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(1);
    }

}
