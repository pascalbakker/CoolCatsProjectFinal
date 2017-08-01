package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.InputStream;
import java.util.List;
import android.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by bakkerp on 6/30/2017.
 */

public class SavedPostsFragment extends Fragment {
    Bitmap defaultImage;
    Bitmap currentImage;
    ListObjectSaved adapter;
    List<ListItemSaved> listSaved;
    ListView  listViewSaved;
    View rootView;
    ImageView imageView;
    Integer index;
    String itemURL;
    String aUrl, postTitle, postDate, postLocation, theUrl;

    FileOutputStream outputStream;
    File file = new File("savedPosts.text");

    public SavedPostsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Fragment: ","SavedPosts has started.");
        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        //The list of items being put into list
        listSaved = new ArrayList<ListItemSaved>();
        index=0;


        //Initialize adapter for GridView
        adapter = new ListObjectSaved(getActivity(), 0 ,listSaved);
        ListItem newListItem = new ListItem();

        /* Initialize Gridview */
        rootView = inflater.inflate(R.layout.saved_posts, container, false);
        listViewSaved = (ListView) rootView.findViewById(R.id.ListViewSaved);
        imageView = (ImageView) rootView.findViewById(R.id.imageHolder2);

        try {
            startListView();
            Log.d("Savedposts","Completed loading saved posts");
        } catch (IOException e) {
            e.printStackTrace();
        }

        listViewSaved.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemSaved itemToDisplay = (ListItemSaved) parent.getItemAtPosition(position);
                //LargePost largePost = new LargePost(itemToDisplay.image,itemToDisplay.name,itemToDisplay.comment);

                Intent i = new Intent(getActivity(), LargePost.class);
                i.putExtra("MyClass", itemToDisplay.url);

                startActivity(i);
            }
        });


        listViewSaved.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void startListView() throws IOException {
        loadUrRLs();
    }

    public void loadUrRLs() throws IOException {
        BufferedReader reader;
        File f = getActivity().getFileStreamPath("SavedPostsInternal.txt");
        FileReader fr = new FileReader(f);
        reader = new BufferedReader(fr);
        // new File("file:///android.asset/xxx.txt")
        String mLine;
        mLine = reader.readLine();
        System.out.print(mLine);
        while(mLine != null){
            Log.d("Read Line: ",mLine);
            try{
                requestPost(mLine);
                mLine = reader.readLine();

            }catch (Exception e){
                Log.e("Loading image in save",e.toString());
                Log.v("Error ","Could not load saved url");
            }
        }
    }


    private void requestPost(String galleryURL){
        SavedPostsFragment context = this;
        aUrl = galleryURL;

        //imageView.buildDrawingCache();
        //Bitmap bitmap = imageView.getDrawingCache();
        //newListItem.image = bitmap;
        //Source https://stackoverflow.com/questions/42879748/bitmap-is-null-when-convert-imageview-in-bitmap
        //============

        theUrl ="http://18.220.32.41:3001/image?name="+ galleryURL;
        Log.d("URL Pulled: ",theUrl);
        Picasso.with(getActivity())
                .load(theUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //if( galleryIndex < gallery.length-1) {
                        // Drawable is ready
                        currentImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        String imageinfo_url ="http://18.220.32.41:3001/imageinfo?name=" + aUrl; //Image info url
                        postTitle = "No link";
                        StringRequest stringRequest = new StringRequest( Request.Method.GET, imageinfo_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //System.out.println(response);
                                        String[] array = response.split("\\|");
                                        //tv.setText(response);
                                        if( array.length >=5) {
                                            Log.d("Getting info","Yes");
                                            String imageName = array[0];
                                            String location = array[1];
                                            String date = array[2];
                                            String title = array[3];
                                            String tag = array[4];
                                            postTitle = title;
                                            postDate = date;
                                            postLocation = location;
                                            ListItemSaved newListItem = new ListItemSaved();
                                            newListItem.image = currentImage;
                                            newListItem.name = postTitle;
                                            newListItem.comment = postDate;
                                            newListItem.url = aUrl;
                                            listSaved.add(newListItem);
                                            adapter.notifyDataSetChanged();
                                            //System.out.println(imageName + "," + location + "," + date + "," + title + "," + tag);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //tv.setText("Something went wrong" + error.toString());
                                        postTitle = "Could Not Load Title";
                                        postDate = "Could Not Load Date";
                                        postLocation = "No Location";
                                    }
                                });

                        requestQueue.add(stringRequest);

                        // }

                    }

                    @Override
                    public void onError() {

                        currentImage= defaultImage;

                        ListItemSaved newListItem = new ListItemSaved();
                        newListItem.image=currentImage;
                        newListItem.name = postTitle;
                        newListItem.comment= postDate;
                        newListItem.url = "None";
                        //theUrl = newListItem.url;
                        listSaved.add(newListItem);

                        adapter.notifyDataSetChanged();

                    }
                });
        imageView.setImageBitmap(defaultImage);
    }

}




