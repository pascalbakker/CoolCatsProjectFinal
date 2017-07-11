package com.example.bakkerp.coolcatsproject;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.io.Serializable;

/**
 * Created by bakkerp on 6/15/2017.
 */

public class DataClass implements Serializable {
    private String postTitle,postDate;
    private Bitmap postImage;
    public DataClass(String postTitle,String postDate){
        this.postTitle=postTitle;
        this.postDate=postDate;
    }
    public DataClass(String postTitle,String postDate,Bitmap postImage){
        this.postTitle=postTitle;
        this.postDate=postDate;
        this.postImage=postImage;
    }
    public Bitmap getImage(){
        return postImage;
    }
    public String getPostTitle(){
        return postTitle;
    }
    public String getPostDate(){
        return postDate;
    }
    public void setImage(Bitmap newImage){
        this.postImage  = newImage;
    }
    public void setDate(String newDate){
        this. postDate=newDate;
    }
}
