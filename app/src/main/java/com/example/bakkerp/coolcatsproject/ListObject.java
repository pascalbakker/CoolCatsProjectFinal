package com.example.bakkerp.coolcatsproject;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bakkerp on 6/30/2017.
 */

public class ListObject extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;
    public ListObject(Context context, int rid, List<ListItem> list){
        super(context, rid, list);
        mInflater =
                (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        // Retrieve data
        ListItem item = (ListItem)getItem(position);
        final String itemURL = item.url;
        // Use layout file to generate View
        View view = mInflater.inflate(R.layout.list_item, null);
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
        Button saveButton;
        saveButton = (Button) view.findViewById(R.id.savePost);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SavedPostsFragment.add(itemURL);
            }
        });
        item.savePost = saveButton;
        return view;
    }
}