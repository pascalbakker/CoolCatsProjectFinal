package com.example.bakkerp.coolcatsproject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by bakkerp on 6/30/2017.
 */

public class ListObjectSaved extends ArrayAdapter<ListItemSaved> {
    private LayoutInflater mInflater;
    String itemURL;
    public ListObjectSaved(Context context, int rid, List<ListItemSaved> list){
        super(context, rid, list);
        mInflater =
                (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        // Retrieve data
        final ListItemSaved item = (ListItemSaved)getItem(position);
        itemURL = item.url;
        // Use layout file to generate View
        View view = mInflater.inflate(R.layout.list_item_saved, null);
        // Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.image);
        image.setImageBitmap(item.image);
        // Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(item.name);
        // Set comment
        TextView comment;
        comment = (TextView) view.findViewById(R.id.comment);
        comment.setText(item.comment);
        Button removeButton;
        removeButton = (Button) view.findViewById(R.id.removePost);
        removeButton.setOnClickListener(new View.OnClickListener() {
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

                }catch (Exception e){e.printStackTrace();}
            }
        });
        item.removePost = removeButton;
        return view;
    }
}