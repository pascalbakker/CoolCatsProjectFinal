package com.example.bakkerp.coolcatsproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
public class CreatePage extends AppCompatActivity
{
    public static final int IMAGE_GALLERY_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    private ImageView imgPic;
    private EditText tv, tv1, tv2;
    private String imgTitle, date, loc;
    private Button btn1, btn2, btn3;
    private ImageButton btnX;
    private Bitmap bitmap;

    String UPLOAD_URL = "http://18.220.32.41:3001/post";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);

        imgPic = (ImageView)findViewById(R.id.imgPic);
        btn1 = (Button) findViewById(R.id.camera);
        btn2 = (Button) findViewById(R.id.library);
        btn3 = (Button) findViewById(R.id.submit);
        btnX = (ImageButton) findViewById(R.id.close);

        btnX.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickClose();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OnTakePhotoClicked(v);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onImageGalleryClicked(v);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSubmissionClicked();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode==IMAGE_GALLERY_REQUEST)
            {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try
                {
                    inputStream= getApplicationContext().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imgPic.setImageBitmap(bitmap);

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(CreatePage.this, "Unable to open Image", Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode==CAMERA_REQUEST)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgPic.setImageBitmap(bitmap);
            }
        }
    }

    private void onClickClose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePage.this);
        builder.setTitle("Are you sure you want to close?");

        builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CreatePage.this, HomePage.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public void onImageGalleryClicked(View view) {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = picDir.getPath();

        Uri data = Uri.parse(path);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    public void OnTakePhotoClicked(View view) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST);
    }

    public void onSubmissionClicked() {


        tv = (EditText) findViewById(R.id.title);
        tv1 = (EditText) findViewById(R.id.date);
        tv2 = (EditText) findViewById(R.id.location);
        imgTitle = tv.getText().toString();
        date = tv1.getText().toString();
        loc = tv2.getText().toString();
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(CreatePage.this, s, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //textView.setText("Something went wrong" + error.toString());
                Toast.makeText(CreatePage.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("date", date);
                params.put("location", loc);
                params.put("title", imgTitle);

                //returning parameters
                return params;
            }
        };
/*

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
*/
        //Adding request to the queue
        requestQueue.add(stringRequest);

        Intent intent = new Intent(CreatePage.this, HomePage.class);
        startActivity(intent);
    }

    public String getStringImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}