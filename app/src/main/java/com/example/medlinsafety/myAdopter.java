package com.example.medlinsafety;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class myAdopter extends ArrayAdapter<String> {
    String[] mymsg;
    String[] mydate;

    Activity myactivity;
    public myAdopter(@NonNull Activity context, String[] msg, String[] date) {
        super(context, R.layout.activity_custom_list,msg);
        mymsg=msg;
        mydate=date;
        myactivity=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= myactivity.getLayoutInflater();
        View myview =inflater.inflate(R.layout.activity_custom_list,null,true);
        TextView tx1=(TextView) myview.findViewById(R.id.mainHeading);
        TextView tx2=(TextView) myview.findViewById(R.id.subHeading);
        ImageView im1=(ImageView) myview.findViewById(R.id.img);
        tx1.setText(mymsg[position].split(",")[0]);
        tx2.setText(mydate[position]);
        im1.setImageResource(R.drawable.map);
        return myview;
    }
}
