package com.example.bakkerp.coolcatsproject;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


//This is where users go when a post is clicked on the list
//I need to change it to ViewPost for clarity
public class LargePost extends AppCompatActivity {
    private Bitmap imageFromPost;
    private String titleTitleFromPost;

    public LargePost() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_post);
        //Bundle b = this.getIntent().getExtras();
        /*
        ImageView image = (ImageView) findViewById(R.id.postTitle);
        //image.setImageBitmap(imageFromPost);
        TextView text = (TextView) findViewById(R.id.postTitle);
        //text.setText(titleTitleFromPost);
        */
        // Inflate the layout for this fragment
    }

}

