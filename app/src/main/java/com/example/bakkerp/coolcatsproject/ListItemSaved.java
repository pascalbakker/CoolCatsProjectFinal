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

public class ListItemSaved{
    public Bitmap image;
    public String name;
    public String comment;
    public Button removePost;
    public String url;
}


