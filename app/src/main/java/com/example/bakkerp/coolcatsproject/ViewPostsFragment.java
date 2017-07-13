package com.example.bakkerp.coolcatsproject;

import android.app.Fragment;
import android.app.LauncherActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class ViewPostsFragment extends Fragment {
    Bitmap defaultImage;
    ListObject adapter;
    List<ListItem> list;
    ListView listView;
    View rootView;
    ImageView imageView;


    public ViewPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.download);


        //The list of items being put into list
        list = new ArrayList<ListItem>();



        //Initialize adapter for GridView
        adapter = new ListObject(getActivity(), 0 , list);
        list.add(requestPost());
        //startListView();


        /* Initialize Gridview */
        rootView = inflater.inflate(R.layout.fragment_view_posts, container, false);
        listView = (ListView) rootView.findViewById(R.id.ListView01);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem itemToDisplay = (ListItem) parent.getItemAtPosition(position);
                //LargePost largePost = new LargePost(itemToDisplay.image,itemToDisplay.name,itemToDisplay.comment);

                Intent i = new Intent(getActivity(), LargePost.class);
                startActivity(i);
            }
        });
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(page);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        list.add(requestPost());
        /*
        ListItem item1 = new ListItem();
        item1.image = defaultImage;
        item1.name = "David";
        item1.comment = "Boston is not snowing now.";
        list.add(item1);
        */
        adapter.notifyDataSetChanged();
    }

    public void startListView(){
        for(int i=0;i<11;i++){
            list.add(requestPost());
        }
        adapter.notifyDataSetChanged();
    }

    public ListItem requestPost(){
        Integer index = 0;
        System.out.println("server");
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String imageurl ="http://httpbin.org/image/jpeg?name=" + index.toString();
        ImageRequest imageRequest = new ImageRequest( imageurl,
                //asynchronous request to the server
                //to recieve from the server
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //System.out.println(response);
                        imageView.setImageBitmap(response);

                    }
                    //no response error
                },0,0,null,null);
        //adds the request to the server queue
        //volley sends the request
        ListItem newListItem = new ListItem();
        //imageView.buildDrawingCache();
        //Bitmap bitmap = imageView.getDrawingCache();
        //newListItem.image = bitmap;
        newListItem.image = defaultImage;
        newListItem.name = "Test";
        newListItem.comment= "2/2/2";
        requestQueue.add(imageRequest);
        return newListItem;
    }
}

