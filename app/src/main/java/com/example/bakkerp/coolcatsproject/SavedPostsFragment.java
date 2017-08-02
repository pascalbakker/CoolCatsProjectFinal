package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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
    ListObject adapter;
    List<ListItem> listSaved;
    ListView  listViewSaved;
    View rootView;
    ImageView imageView;
    Integer index;
    FileOutputStream outputStream;
    File file = new File("savedPosts.text");

    public SavedPostsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Fragment: ","SavedPosts has started.");
        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        imageView = (ImageView) getActivity().findViewById(R.id.imgPic);
        //The list of items being put into list
        listSaved = new ArrayList<ListItem>();
        index=0;


        //Initialize adapter for GridView
        adapter = new ListObject(getActivity(), 0 ,listSaved);
        ListItem newListItem = new ListItem();

        /* Initialize Gridview */
        rootView = inflater.inflate(R.layout.saved_posts, container, false);
        listViewSaved = (ListView) rootView.findViewById(R.id.ListViewSaved);
        try {
            startListView();
            Log.d("Savedposts","Completed loading saved posts");
        } catch (IOException e) {
            e.printStackTrace();
        }

        listViewSaved.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem itemToDisplay = (ListItem) parent.getItemAtPosition(position);
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


//https://stackoverflow.com/questions/24291721/reading-a-text-file-line-by-line-in-android
    public static void add(String url){
        try
        {
            File file = new File("savedPosts.text");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(url);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    //https://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
    public void delete(String url) throws IOException {
        File inputFile = new File("savedPosts.text");
        File tempFile = new File("myTempPosts.text");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != "") {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(url)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile);
    }

    public void loadUrRLs() throws IOException {
        FileInputStream is;
        BufferedReader reader;
        final File file = new File(Environment.getExternalStorageDirectory().getPath(),"SavedPosts.txt");
        Log.d("File exists? ","Testing "+file.exists());
        if (file.exists()) {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            Log.v("","test"+(line!=null));
            while(line != null){
                Log.d("StackOverflow", line);
                line = reader.readLine();
                addItem(line);
            }
        }

    }

    public void addItem(String url){
        ListItem newListItem = new ListItem();
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

                    }
                });
        newListItem.image=currentImage;
        newListItem.name = "Test111"+index.toString();
        newListItem.comment= "2/2/21";
        newListItem.url = url;
        listSaved.add(newListItem);
        adapter.notifyDataSetChanged();
    }
}




