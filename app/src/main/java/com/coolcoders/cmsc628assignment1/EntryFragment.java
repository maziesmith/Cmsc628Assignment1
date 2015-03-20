package com.coolcoders.cmsc628assignment1;

import android.app.ListFragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * List UI that contains entries
 */
public class EntryFragment extends ListFragment {

    private static List<EntryItem> _items;

    // TODO: populate list from MainActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _items = new ArrayList<EntryItem>();
        //Resources resources = getResources();

        long dummy = System.currentTimeMillis();
        _items.add(new EntryItem("Entry 1", dummy-120000,dummy));
        _items.add(new EntryItem("Entry 2", dummy,dummy+120000));
        _items.add(new EntryItem("Entry 3", dummy+120000,dummy+240000));
        _items.add(new EntryItem("Entry 4", dummy+240000,dummy+360000));
        _items.add(new EntryItem("Entry 5", dummy+360000,dummy+480000));
        _items.add(new EntryItem("Entry 6", dummy+480000,dummy+600000));

        // send data to the adapter for displaying
        setListAdapter(new EntryAdapter(getActivity(), _items));
    }

    public void AddNewItem(EntryItem item) {
        _items.add(item);
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
