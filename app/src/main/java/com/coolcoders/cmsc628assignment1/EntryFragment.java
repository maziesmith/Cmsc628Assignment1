package com.coolcoders.cmsc628assignment1;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import java.util.ArrayList;
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
        Resources resources = getResources();

        _items.add(new EntryItem("first", 1, 10));
        _items.add(new EntryItem("second", 11, 20));
        _items.add(new EntryItem("third", 21, 30));
        _items.add(new EntryItem("fourth", 31, 40));
        _items.add(new EntryItem("fifth", 41, 50));

        // send data to the adapter for displaying
        setListAdapter(new EntryAdapter(getActivity(), _items));
    }

}
