package com.example.iotcontrol.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.GraphView;
import com.example.iotcontrol.R;

public class StatisticsFragment extends Fragment {
    GraphView tempGraph;
    GraphView humGraph;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_statistics, container, false);

        tempGraph = rootView.findViewById(R.id.graphTemp);
        humGraph = rootView.findViewById(R.id.graphHum);

        String [] data = {"1.11", "2.11", "3.11", "4.11", "5.11", "6.11", "7.11", "8.11", "9.11", "10.11","11.11", "12.11"};
        float [] temp = {22, 25, 22.5f,22.4f, 23, 21, 21.5f, 22, 24, 23, 24, 26, 25};
        float [] hum = {50, 55, 60, 56, 57, 60, 65, 56, 55, 54, 60, 80};


        tempGraph.setAttr("temp", data, temp);
        humGraph.setAttr("hum", data, hum);

        return rootView;
    }
}