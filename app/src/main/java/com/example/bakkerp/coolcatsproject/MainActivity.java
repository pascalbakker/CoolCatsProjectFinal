package com.example.bakkerp.coolcatsproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        public void sendMessage(View view) {
            Intent intent = new Intent(this,HomePage.class);
            EditText editText = (EditText) findViewById(R.id.HomePage);
            String message = editText.getText().toString();
            startActivity(intent);
        }
    }

}
