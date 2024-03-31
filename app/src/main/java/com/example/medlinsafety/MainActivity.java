package com.example.medlinsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.app.ActivityCompat;
import androidx.core.util.AndroidXConsumerKt;

import android.Manifest;
import android.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Bitmap image;
    FragmentManager fm;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Saving state of our app
        // using SharedPreferences
        sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
         editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);

        }

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 102);
        }
        fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        Fragment home =new HomeScreen();
        fragmentTransaction.replace(R.id.fram, home);
        //fragmentTransaction.addToBackStack("home");
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        optionsMenu = menu;
        sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {

            optionsMenu.getItem(0).setIcon(getResources()
                    .getDrawable(R.drawable.off));
            optionsMenu.getItem(0).setTitle("dark");
        }
        else {
            optionsMenu.getItem(0).setIcon(getResources()
                    .getDrawable(R.drawable.on));
            optionsMenu.getItem(0).setTitle("light");
        }
        return true;
    }
    Menu optionsMenu;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.profile:
                fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransactionProfile = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                Fragment profile =new Profile();
                fragmentTransactionProfile.replace(R.id.fram, profile);
                fragmentTransactionProfile.addToBackStack("profile");
                fragmentTransactionProfile.commit(); // save the changes
                break;
            case R.id.settings:
                fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransactionSetting = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                Fragment setting =new Setting();
                fragmentTransactionSetting.replace(R.id.fram, setting);
                fragmentTransactionSetting.addToBackStack("setting");
                fragmentTransactionSetting.commit(); // save the changes
                break;
            case R.id.log:
                fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransactionLog = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                Fragment log =new HelpLog();
                fragmentTransactionLog.replace(R.id.fram, log);
                fragmentTransactionLog.addToBackStack("setting");
                fragmentTransactionLog.commit(); // save the changes
                break;
            case R.id.msg:
                fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransactionMsg = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                Fragment msg =new Women_website();
                fragmentTransactionMsg.replace(R.id.fram, msg);
                fragmentTransactionMsg.addToBackStack("msg");
                fragmentTransactionMsg.commit(); // save the changes
                break;
            case R.id.darkMode:

            if(item.getTitle().equals("light"))
            {

                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                optionsMenu.getItem(0).setIcon(getResources()
                        .getDrawable(R.drawable.off));
                editor.putBoolean(
                        "isDarkModeOn", true);
                editor.apply();
                item.setTitle("dark");
            }
            else
            {

                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                optionsMenu.getItem(0).setIcon(getResources()
                        .getDrawable(R.drawable.on));
                editor.putBoolean(
                        "isDarkModeOn", false);
                editor.apply();
                item.setTitle("light");
            }
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}