package com.example.bakkerp.coolcatsproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

//Home
public class HomePage extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private RelativeLayout mDrawerRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] mDrawerOptionLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        //Puts savedpost.txt in internal storage
        AssetManager am = this.getAssets();
        try {
            InputStream inputStream = am.open("SavedPosts.txt");
            File f = this.getFileStreamPath("SavedPostsInternal.txt");
            OutputStream outputStream = new FileOutputStream(f);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ich = Channels.newChannel(inputStream);
            WritableByteChannel och = Channels.newChannel(outputStream);

            while (ich.read(buffer) > -1 || buffer.position() > 0)
            {
                buffer.flip();
                och.write(buffer);
                buffer.compact();
            }
            ich.close();
            och.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Populating ListView and Handling Selection
        Resources resources = getResources();
        mDrawerOptionLabels = resources.getStringArray(R.array.sliding_drawer_array);
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerOptionLabels);

        mDrawerListView.setAdapter(drawerAdapter);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fm = getFragmentManager();
                Fragment fragment = new Fragment();
                Intent i;
                switch(position){
                    case 0:
                        fragment = new ViewPostsFragment();
                        break;
                    case 1:
                        i = new Intent(HomePage.this, CreatePage.class);
                        startActivity(i);
                        break;
                    case 2:
                        fragment = new SavedPostsFragment();
                        break;
                }

                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                setTitle(mDrawerOptionLabels[position]);
                mDrawerListView.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
            }
        });

        if(savedInstanceState == null){
            FragmentManager fm = getFragmentManager();
            Fragment fragment = new ViewPostsFragment();
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(mDrawerOptionLabels[0]);
        }
    } // End of onCreate()

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
