package com.example.bakkerp.coolcatsproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by wahlstrome on 6/23/2017.
 */

public class CreatePage extends Navigation
{


    public static final int IMAGE_GALLERY_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    private ImageView imgPic;
    private EditText tv1, tv2;
    private String date, loc;
    private Bitmap bitmap;


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

                    bitmap = BitmapFactory.decodeStream(inputStream);

                    imgPic.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open Image", Toast.LENGTH_LONG).show();

                }
            }
            else if(requestCode==CAMERA_REQUEST)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgPic.setImageBitmap(bitmap);
            }

            tv1 = (EditText) findViewById(R.id.date);
            tv2 = (EditText) findViewById(R.id.location);
            date = tv1.getText().toString();
            loc = tv2.getText().toString();
        }
    }

    public void OnTakePhotoClicked(View view)
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST);

    }

    public void onSubmissionClicked()
    {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(CreatePage.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CreatePage.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });


          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage();

                //Getting Image Name
                String name = editTextName.getText().toString();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }*/


        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}