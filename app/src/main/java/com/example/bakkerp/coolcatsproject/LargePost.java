package com.example.bakkerp.coolcatsproject;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;


//This is where users go when a post is clicked on the list
//I need to change it to ViewPost for clarity
public class LargePost extends AppCompatActivity implements Serializable{
    private Bitmap currentImage;
    private String titleTitleFromPost;
    private ImageButton back;
    public LargePost() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_post);
        String url = (String) getIntent().getSerializableExtra("MyClass");
        //String url = "http://18.220.32.41:3001/image?name="+(String) getIntent().getSerializableExtra("MyClass");
        ImageView imageView = (ImageView) findViewById(R.id.postImage);
        //back = (ImageButton) findViewById(R.id.back);


        Picasso.with(this)
                .load(url)
                .into(imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_large_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}