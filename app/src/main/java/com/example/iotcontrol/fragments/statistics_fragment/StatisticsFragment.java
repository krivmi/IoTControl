package com.example.iotcontrol.fragments.statistics_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.R;
import com.example.iotcontrol.ServerConnector;

public class StatisticsFragment extends Fragment {
    GraphView tempGraph;
    GraphView humGraph;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            String [] data = msg.getData().getStringArray("dayTimes");
            double [] temp = msg.getData().getDoubleArray("avg_temps");
            double [] hum = msg.getData().getDoubleArray("avg_hums");

            tempGraph.setAttr("temp", data, temp, true);
            humGraph.setAttr("hum", data, hum, true);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_statistics, container, false);

        tempGraph = rootView.findViewById(R.id.graphTemp);
        humGraph = rootView.findViewById(R.id.graphHum);

        String url = "http://adelakrivankova.wz.cz/php/temphum/week_values.php";
        ServerConnector sc = new ServerConnector(handler, url, "WEEK_STATS", 20000);
        sc.start();

        String [] data = {"0.0"};
        double [] temp = {0};
        double [] hum = {0};

        tempGraph.setAttr("temp", data, temp, false);
        humGraph.setAttr("hum", data, hum, false);

        return rootView;
    }
}
