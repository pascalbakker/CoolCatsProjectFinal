package com.example.bakkerp.coolcatsproject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Navigation {
    List<ListItem> list;
    Bitmap defaultImage;
    ListItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        list = new ArrayList<ListItem>();
        // ... the usual
        // Attach the listener to the AdapterView onCreate
        EndlessScrollListener scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(page);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        };
        ListView lvItems = (ListView) findViewById(R.id.postList);


        defaultImage =
                BitmapFactory.decodeResource(getResources(), R.drawable.download);
        // Create testing data
        ListItem item1 = new ListItem();
        item1.image = defaultImage;
        item1.name = "David";
        item1.comment = "Boston is not snowing now.";
        list.add(item1);
        ListItem item2 = new ListItem();
        item2.image = defaultImage;
        item2.name = "Cooper";
        item2.comment = "The design is so cool";
        list.add(item2);
        ListItem item3 = new ListItem();
        item3.image = defaultImage;
        item3.name = "Jones";
        item3.comment = "I like hacking. Do you like it?";
        list.add(item3);
        list.add(item3);
        list.add(item3);
        list.add(item3);
        list.add(item3);

        list.add(item3);
        list.add(item3);
        adapter = new ListItemAdapter(this, 0, list);
        lvItems.setAdapter(adapter);

        // Assign ListItemAdapter to ListView
        lvItems.setOnScrollListener(scrollListener);
    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        ListItem item1 = new ListItem();
        item1.image = defaultImage;
        item1.name = "David";
        item1.comment = "Boston is not snowing now.";
        list.add(item1);
        adapter.notifyDataSetChanged();
    }

}


