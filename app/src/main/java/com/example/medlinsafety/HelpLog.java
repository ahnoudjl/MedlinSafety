package com.example.medlinsafety;

import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpLog extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String[] dates;
    String[] texts;
    public HelpLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpLog.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpLog newInstance(String param1, String param2) {
        HelpLog fragment = new HelpLog();
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
    DbHelper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_help_log, container, false);
        helper=new DbHelper(getContext());
        ArrayList<String>[] lists=helper.getAllLog();
        texts=new String[lists[0].size()];
        dates=new String[lists[1].size()];
        int c=0;
        for (String x:lists[0]) {
            texts[c++]=x;
        }
        c=0;
        for (String x:lists[1]) {
            dates[c++]=x;
        }
        myAdopter adopter=new myAdopter(getActivity(),texts,dates);
        ListView listView= v.findViewById(R.id.lv1);
        listView.setAdapter(adopter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String subText[]=texts[i].split("https://www.google.com/maps/@");
                String cordsLat=(subText[1].split(","))[0];
                String cordsLan=(subText[1].split(","))[1];
                Intent intent = new Intent(getContext(),MapsActivity.class);
                intent.putExtra("lat",cordsLat);
                intent.putExtra("lon",cordsLan);
                //Toast.makeText(getContext(),cordsLan+"   "+cordsLat, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        return v;
    }
}