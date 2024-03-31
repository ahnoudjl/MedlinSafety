package com.example.medlinsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.logo);
            DbHelper helper =new DbHelper(this);
            int no=helper.numberOfRows("settings");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent;
                    if(no==0) {
                        intent = new Intent(getApplicationContext(), Signup.class);
                    }
                    else
                    {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}