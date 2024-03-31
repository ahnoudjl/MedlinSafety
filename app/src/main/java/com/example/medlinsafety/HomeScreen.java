package com.example.medlinsafety;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends Fragment {
    public HomeScreen() {
        // Required empty public constructor
    }
    SmsManager sms;
    String phrase;
    String no;
    DbHelper db;
    FusedLocationProviderClient client;
    String my_loc="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home_screen, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        db=new DbHelper(container.getContext());
        Cursor cr=db.getData("select * from settings");
        cr.moveToFirst();

        no=cr.getString(1);
        phrase=cr.getString(2);
        ImageView img=v.findViewById(R.id.help_image);
        img.setImageResource(R.drawable.help);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sms =SmsManager.getDefault();
                if (ContextCompat.checkSelfPermission(
                        getActivity(),
                        Manifest.permission
                                .ACCESS_FINE_LOCATION)
                        == PackageManager
                        .PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                        getActivity(),
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION)
                        == PackageManager
                        .PERMISSION_GRANTED) {
                    // When permission is granted
                    // Call method
                    my_loc=getCurrentLocation();
                }
                else {
                    // When permission is not granted
                    // Call method
                    requestPermissions(
                            new String[] {
                                    Manifest.permission
                                            .ACCESS_FINE_LOCATION,
                                    Manifest.permission
                                            .ACCESS_COARSE_LOCATION },
                            100);
                }
                try {
                    Thread.sleep(2000);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "My Channel";
                        String description ="This is my channel";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("MyID", name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system; you can't change the importance
                        // or other notification behaviors after this
                        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getContext(),"MyID")
                                    .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                                    .setContentTitle("Medlin Saftety App")
                                    .setContentText("Your message has been sent")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Intent notificationIntent = new Intent(getContext(), MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        return v;
    }
    String loc="";
    @SuppressLint("MissingPermission")
    private String getCurrentLocation()
    {

        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)getActivity()
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task)
                        {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                              //loc=String.valueOf(location.getLatitude());
                                // set longitude
                                //loc=loc+","+String.valueOf(location.getLongitude());
                                sms.sendTextMessage(no, null, phrase+", "+getString(R.string.help_text)+" https://maps.google.com/?q="+String.valueOf(location.getLatitude())+","+ String.valueOf(location.getLongitude()), null,null);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                db.curdOperations("insert into logs(log_date,log_no) values(\""+ currentDateandTime+"\",\""+phrase+", "+getString(R.string.help_text)+" https://www.google.com/maps/@"+String.valueOf(location.getLatitude())+ ","+String.valueOf(location.getLongitude())+"\")");
                               // Toast.makeText(getContext(),"Msg Sent",Toast.LENGTH_LONG).show();
                            }
                            else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest
                                        = new LocationRequest()
                                        .setPriority(
                                                LocationRequest
                                                        .PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult)
                                    {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();
                                        // Set latitude
                                        loc=String.valueOf(location1.getLatitude());
                                        // Set longitude
                                        loc=loc+","+ String.valueOf(location1.getLongitude());
                                        sms.sendTextMessage(no, null, phrase+", "+getString(R.string.help_text)+" https://www.google.com/maps/@"+String.valueOf(location1.getLatitude())+","+ String.valueOf(location1.getLongitude()), null,null);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss ");
                                        String currentDateandTime = sdf.format(new Date());
                                        db.curdOperations("insert into logs(log_date,log_no) values(\""+ currentDateandTime+"\",\""+phrase+", "+getString(R.string.help_text)+" https://www.google.com/maps/@"+String.valueOf(location1.getLatitude())+ ","+String.valueOf(location1.getLongitude())+"\")");
                                      //  Toast.makeText(getContext(),"Msg Sent",Toast.LENGTH_LONG).show();
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(
                            Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        //Toast.makeText(getContext(),"Location:"+loc,Toast.LENGTH_LONG).show();
        return loc;
    }
}