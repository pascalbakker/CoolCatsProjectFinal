package com.example.bakkerp.coolcatsproject;

import android.media.Image;
import android.provider.ContactsContract;

import java.io.Serializable;

/**
 * Created by bakkerp on 6/15/2017.
 */

public class DataClass implements Serializable {
    private String postTitle,postDate;
    private Image postImage;
    public DataClass(String postTitle,String postDate){
        this.postTitle=postTitle;
        this.postDate=postDate;
    }
    public DataClass(String postTitle,String postDate,Image postImage){
        this.postTitle=postTitle;
        this.postDate=postDate;
        this.postImage=postImage;
    }
    public Image getImage(){
        return postImage;
    }
    public String getPostTitle(){
        return postTitle;
    }
    public String getPostDate(){
        return postDate;
    }
    public void setImage(Image newImage){
        this.postImage  = newImage;
    }
    public void setDate(String newDate){
        this. postDate=newDate;
    }
}
