package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPostsFragment extends Fragment {
    Bitmap defaultImage;
    Bitmap currentImage;
    ListObject adapter;
    List<ListItem> list;
    ListView listView;
    View rootView;
    ImageView imageView;
    Integer index;
    String theUrl, postTitle, postDate;


    public ViewPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        imageView = (ImageView) getActivity().findViewById(R.id.imgPic);
        //The list of items being put into list
        list = new ArrayList<ListItem>();
        index=0;


        //Initialize adapter for GridView
        adapter = new ListObject(getActivity(), 0 , list);
        ListItem newListItem = new ListItem();

        /* Initialize Gridview */
        rootView = inflater.inflate(R.layout.fragment_view_posts, container, false);
        listView = (ListView) rootView.findViewById(R.id.ListView01);
        imageView = (ImageView) rootView.findViewById(R.id.imageHolder);
        startListView();

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem itemToDisplay = (ListItem) parent.getItemAtPosition(position);
                //LargePost largePost = new LargePost(itemToDisplay.image,itemToDisplay.name,itemToDisplay.comment);

                Intent i = new Intent(getActivity(), LargePost.class);
                i.putExtra("MyClass", itemToDisplay.url);

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
                adapter.notifyDataSetChanged();

                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    private void loadNextDataFromApi(int offset) {
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

    private void startListView(){
        for(int i=0;i<1;i++){
            list.add(requestPost());
            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    private ListItem requestPost(){
        ViewPostsFragment context = this;
        ListItem newListItem = new ListItem();
        //imageView.buildDrawingCache();
        //Bitmap bitmap = imageView.getDrawingCache();
        //newListItem.image = bitmap;
        //Source https://stackoverflow.com/questions/42879748/bitmap-is-null-when-convert-imageview-in-bitmap
        //============
        String url ="http://18.220.32.41:3001/image?name="+index.toString()+".png";
        Picasso.with(getActivity())
                .load(url)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Drawable is ready
                        currentImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    }

                    @Override
                    public void onError() {

                    }
                });
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String imageinfo_url ="http://18.220.32.41:3001/imageinfo?name=" + index.toString() + ".png";
        StringRequest stringRequest = new StringRequest( Request.Method.GET, imageinfo_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        String[] array = response.split("\\|");
                        //tv.setText(response);
                        if( array.length >=5) {
                            String imageName = array[0];
                            String location = array[1];
                            String date = array[2];
                            String title = array[3];
                            Log.v("title", title);
                            String tag = array[4];
                            postTitle = title;
                            postDate = date;
                            //System.out.println(imageName + "," + location + "," + date + "," + title + "," + tag);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //tv.setText("Something went wrong" + error.toString());
                        postTitle = "Error";
                        postDate = "Error";
                    }
                });

        requestQueue.add(stringRequest);

        newListItem.image=currentImage;
        newListItem.name = postTitle;
        newListItem.comment= postDate;
        newListItem.url = url;
        theUrl = newListItem.url;
        index++;

        return newListItem;
    }
}

