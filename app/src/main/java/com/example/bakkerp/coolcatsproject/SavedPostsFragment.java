package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import android.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    String postTitle, postDate, postLocation;

    FileOutputStream outputStream;
    File file = new File("savedPosts.text");

    public SavedPostsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Fragment: ","SavedPosts has started.");
        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        imageView = (ImageView) getActivity().findViewById(R.id.imageHolder);
        //The list of items being put into list
        listSaved = new ArrayList<ListItemSaved>();
        index=0;


        //Initialize adapter for GridView
        adapter = new ListObjectSaved(getActivity(), 0 ,listSaved);
        ListItem newListItem = new ListItem();

        /* Initialize Gridview */
        rootView = inflater.inflate(R.layout.saved_posts, container, false);
        listViewSaved = (ListView) rootView.findViewById(R.id.ListViewSaved);
        imageView = (ImageView) rootView.findViewById(R.id.imageHolder);

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
        while((mLine = reader.readLine()) != null){
            Log.v("Read Line: ",mLine);
            try{
                addItem(mLine);
            }catch (Exception e){
                Log.e("Loading image in save",e.toString());
                Log.v("Error ","Could not load saved url");
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        adapter.notifyDataSetChanged();
    }

    public void addItem(String url) throws InterruptedException, ExecutionException {
        ListItemSaved newListItem = new ListItemSaved();
        imageView.setImageBitmap(defaultImage);
        Picasso.with(getActivity())
                .load(url)
                .resize(300,300)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Drawable is ready
                        currentImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    }
                    @Override
                    public void onError() {
                        currentImage = defaultImage;
                        Log.v("Error ","using default image");


                    }
                });
        String imageinfo_url = url.replace("image","imageinfo"); //Image info url
        postTitle = "No link";

        newListItem.image=currentImage;
        newListItem.name = postTitle;
        newListItem.comment= "2/2/21";
        newListItem.url = url;
        itemURL = newListItem.url;
        listSaved.add(newListItem);
        /*
        newListItem.removePost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    File inputFile = v.getContext().getFileStreamPath("SavedPostsInternal.txt");
                    File tempFile = v.getContext().getFileStreamPath("SavedPostsTemp.txt");

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String lineToRemove = itemURL;
                    String currentLine;

                    while ((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if (trimmedLine.equals(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();
                    boolean successful = tempFile.renameTo(inputFile);

                    for(int i=0;i< listSaved.size();i++){
                        if(adapter.getItem(i).url.equals(lineToRemove)){
                            adapter.remove(adapter.getItem(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){e.printStackTrace();}
            }
        });
        */
        adapter.notifyDataSetChanged();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main_actions, menu);

        return getActivity().onCreateOptionsMenu(menu);
    }



}




