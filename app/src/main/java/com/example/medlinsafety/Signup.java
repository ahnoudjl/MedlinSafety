package com.example.medlinsafety;

import androidx.annotation.Nullable;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class Signup extends AppCompatActivity {

    EditText name;
    EditText id;
    EditText phone;
    EditText address;
    EditText email;
    EditText emergency_no;
    EditText phrase;
    ImageView iv;
    Button btn_submit;
    Button btn_select;
    DbHelper dbHelper;
    ImageView btnSpeech;
    byte[] image_bytes=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA}, 103);
        }
        try {
            dbHelper=new DbHelper(this);
            iv = findViewById(R.id.image);
            iv.setImageResource(R.drawable.avatar);
            name = findViewById(R.id.txtName);
            id = findViewById(R.id.txtIdNo);
            phone = findViewById(R.id.txtPhone);
            address = findViewById(R.id.txtAddress);
            email = findViewById(R.id.txtEmail);
            emergency_no = findViewById(R.id.txtEmergencyNo);
            phrase = findViewById(R.id.txtPhrase);
            btn_submit = findViewById(R.id.btnAdd);
            btn_select = findViewById(R.id.btnUpload);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (name.getText().equals("")||id.getText().equals("")||phone.getText().equals("")||address.getText().equals("")||email.getText().equals("")||emergency_no.getText().equals("")||phrase.getText().equals("")||image_bytes==null)
                    {
                        Toast.makeText(getApplicationContext(),"Please Fill in all fields",Toast.LENGTH_LONG).show();
                    }
                    else{
                        //dbHelper.curdOperations("insert into userprofile(user_name, phone, email, user_address, id_no, image) values(\""+name.getText()+"\",\""+phone.getText()+"\",\""+email.getText()+"\",\""+address.getText()+"\",\""+id.getText()+"\",\""+image_bytes+"\")");
                        boolean a= dbHelper.insertUser(name.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString(),id.getText().toString(),image_bytes);
                        dbHelper.curdOperations("insert into settings (emergency_no,phrase,is_login) values(\""+emergency_no.getText()+"\",\""+phrase.getText()+"\",\"Yes\")");
                        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(newIntent);
                        finish();

                    }
                }
            });
            btnSpeech=findViewById(R.id.btnSpeech);
            btnSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 10);
                    } else {
                        Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder pictureDialog = new AlertDialog.Builder(Signup.this);
                    pictureDialog.setTitle("Select Action");
                    String[] pictureDialogItems = {
                            "Select photo from gallery",
                            "Capture photo from camera" };
                    pictureDialog.setItems(pictureDialogItems,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:

                                            Intent galary=new Intent(Intent.ACTION_PICK);
                                            galary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(galary,1000);
                                            break;
                                        case 1:

                                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent,2000);
                                            break;
                                    }
                                }
                            });
                    pictureDialog.show();
                }
            });
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1000) {
                    iv.setImageURI(data.getData());
                    Bitmap imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    ByteArrayOutputStream objectByteArray=new ByteArrayOutputStream();
                    imageToStore.compress(Bitmap.CompressFormat.JPEG,100,objectByteArray);
                    image_bytes=objectByteArray.toByteArray();
                }
                else if (requestCode == 2000)
                {
                    Bitmap imageToStore= (Bitmap)data.getExtras().get("data");
                    iv.setImageBitmap(imageToStore);
                    ByteArrayOutputStream objectByteArray=new ByteArrayOutputStream();
                    imageToStore.compress(Bitmap.CompressFormat.JPEG,100,objectByteArray);
                    image_bytes=objectByteArray.toByteArray();
                }
                else if (requestCode == 10&& data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    phrase.setText(result.get(0));
                }
            }
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}