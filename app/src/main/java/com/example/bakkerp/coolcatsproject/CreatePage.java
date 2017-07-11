package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by wahlstrome on 6/23/2017.
 */

public class CreatePage extends AppCompatActivity
{


    public static final int IMAGE_GALLERY_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    private ImageView imgPic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
    }

    public void onImageGalleryClicked(View view)
    {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = picDir.getPath();

        Uri data = Uri.parse(path);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        imgPic = (ImageView)findViewById(R.id.imgPic);
        if(resultCode == RESULT_OK)
        {
            if(requestCode==IMAGE_GALLERY_REQUEST)
            {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream= getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imgPic.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open Image", Toast.LENGTH_LONG).show();

                }
            }
            else if(requestCode==CAMERA_REQUEST)
            {
                Bitmap camera = (Bitmap) data.getExtras().get("data");
                imgPic.setImageBitmap(camera);
            }
        }
    }

    public void OnTakePhotoClicked(View view)
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST);

    }

    public void onSubmissionClicked()
    {

    }

}