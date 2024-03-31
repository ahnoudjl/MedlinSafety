package com.example.medlinsafety;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    byte[] imageBytes=null;
    ImageView iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
        DbHelper db=new DbHelper(container.getContext());

        iv=v.findViewById(R.id.image);
        Cursor cr=db.getData("select * from userprofile");
        cr.moveToFirst();
        imageBytes=cr.getBlob(6);
        Bitmap image= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        iv.setImageBitmap(image);

        EditText name=v.findViewById(R.id.txtName);
        EditText id=v.findViewById(R.id.txtIdNo);
        EditText phone=v.findViewById(R.id.txtPhone);
        EditText address=v.findViewById(R.id.txtAddress);
        EditText email=v.findViewById(R.id.txtEmail);

        name.setText(cr.getString(1));
        phone.setText(cr.getString(2));
        email.setText(cr.getString(3));
        address.setText(cr.getString(4));
        id.setText(cr.getString(5));

        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        id.setEnabled(false);
        Button btnImage=v.findViewById(R.id.btnUploadProfile);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(container.getContext());
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
        Button btn=v.findViewById(R.id.btnAddProfile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn.getText().equals(getString(R.string.profile_btn))){
                    name.setEnabled(true);
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    address.setEnabled(true);
                    id.setEnabled(true);
                    btn.setText(R.string.btn_save);
                }
                else{
                    name.setEnabled(false);
                    phone.setEnabled(false);
                    email.setEnabled(false);
                    address.setEnabled(false);
                    id.setEnabled(false);
                    boolean a= db.updateUser(name.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString(),id.getText().toString(),imageBytes);
                    btn.setText(R.string.profile_btn);
                    Toast.makeText(getContext(),R.string.setting_updated,Toast.LENGTH_LONG).show();
                }            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1000) {
                    iv.setImageURI(data.getData());
                    Bitmap imageToStore= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                    ByteArrayOutputStream objectByteArray=new ByteArrayOutputStream();
                    imageToStore.compress(Bitmap.CompressFormat.JPEG,100,objectByteArray);
                    imageBytes=objectByteArray.toByteArray();
                }
                else if (requestCode == 2000)
                {
                    Bitmap imageToStore= (Bitmap)data.getExtras().get("data");
                    iv.setImageBitmap(imageToStore);
                    ByteArrayOutputStream objectByteArray=new ByteArrayOutputStream();
                    imageToStore.compress(Bitmap.CompressFormat.JPEG,100,objectByteArray);
                    imageBytes=objectByteArray.toByteArray();
                }
            }
        }catch (Exception ex)
        {
            Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}