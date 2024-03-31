package com.example.medlinsafety;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText phrase;
    public Setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
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
    ImageView btnSpeech;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10 && data != null) {
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    phrase.setText(result.get(0));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);

        DbHelper db=new DbHelper(container.getContext());


        Cursor cr=db.getData("select * from settings");
        cr.moveToFirst();
        EditText no=v.findViewById(R.id.txtEmergencyNo);
        phrase=v.findViewById(R.id.txtPhrase);
        no.setText(cr.getString(1));
        phrase.setText(cr.getString(2));
        no.setEnabled(false);
        phrase.setEnabled(false);
        btnSpeech = v.findViewById(R.id.btnSpeech);
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(v.getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnUpdate= v.findViewById(R.id.btnUpdateSetting);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnUpdate.getText().equals(getString(R.string.setting_btn))) {
                    no.setEnabled(true);
                    phrase.setEnabled(true);
                    btnUpdate.setText(R.string.btn_save);
                }
                else{
                    no.setEnabled(false);
                    phrase.setEnabled(false);
                    EditText txtno=v.findViewById(R.id.txtEmergencyNo);
                    EditText txtPhrase=v.findViewById(R.id.txtPhrase);
                    db.curdOperations("update settings set emergency_no=\""+txtno.getText()+"\", phrase=\""+txtPhrase.getText()+"\"");
                    btnUpdate.setText(R.string.setting_btn);
                    Toast.makeText(getContext(),R.string.setting_updated,Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}