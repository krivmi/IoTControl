package com.example.iotcontrol.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;

public class HomeFragment extends Fragment {

    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt5;
    TextView txt6;
    TextView txt7;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //txt2.setText("ID = " + String.valueOf(msg.getData().getInt("id")));
            //txt3.setText("PowerStatus = " + String.valueOf(msg.getData().getString("powerStatus")));

            txt4.setText("ID: " + String.valueOf(msg.getData().getInt("id")));
            txt5.setText("Temperature: " + String.valueOf(msg.getData().getLong("temperature")));
            txt6.setText("Humidity: " + String.valueOf(msg.getData().getLong("humidity")));
            txt7.setText("Time: " + String.valueOf(msg.getData().getString("dayTime")));

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ESPConnector esp = new ESPConnector(handler);
        esp.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        //txt2 = rootView.findViewById(R.id.textView2);
        //txt3 = rootView.findViewById(R.id.textView3);
        txt4 = rootView.findViewById(R.id.textView4);
        txt5 = rootView.findViewById(R.id.textView5);
        txt6 = rootView.findViewById(R.id.textView6);
        txt7 = rootView.findViewById(R.id.textView7);


        return rootView;
    }
}