package com.example.bakkerp.coolcatsproject;

/**
 * Created by bakkerp on 7/12/2017.
 */
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;

import java.io.Serializable;

/**
 * Created by bakkerp on 6/30/2017.
 */

public class ListItem{
    public Bitmap image;
    public String name;
    public String comment;
    public Button savePost;
    public String location;
    public String url;
}


