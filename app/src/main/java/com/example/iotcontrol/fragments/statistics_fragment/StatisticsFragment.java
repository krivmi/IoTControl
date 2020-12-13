package com.example.iotcontrol.fragments.statistics_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        int color1 = getResources().getColor(R.color.colorBlack);
        int color2 = getResources().getColor(R.color.colorGraphDots);
        int color3 = getResources().getColor(R.color.colorGraphLines);
        int color4 = getResources().getColor(R.color.colorBlack);

        if(sh.getBoolean("night", false)){
            color1 = getResources().getColor(R.color.colorAqua);
            color2 = getResources().getColor(R.color.colorAquaDark);
            color3 = getResources().getColor(R.color.colorAqua);
            color4 = getResources().getColor(R.color.colorAqua);


            LinearLayout _statisticsLayout = (LinearLayout) rootView.findViewById(R.id.statistics_fragment);
            TextView _temp = (TextView) rootView.findViewById(R.id.textViewTemp);
            TextView _hum = (TextView) rootView.findViewById(R.id.textViewHum);

            _statisticsLayout.setBackgroundColor(getResources().getColor(R.color.colorBlack));
            _temp.setBackgroundColor(getResources().getColor(R.color.colorLayout));
            _hum.setBackgroundColor(getResources().getColor(R.color.colorLayout));
            _temp.setTextColor(getResources().getColor(R.color.colorAqua));
            _hum.setTextColor(getResources().getColor(R.color.colorAqua));
            tempGraph.setBackgroundColor(getResources().getColor(R.color.colorBlack));
            humGraph.setBackgroundColor(getResources().getColor(R.color.colorBlack));

        }

        String url = "http://adelakrivankova.wz.cz/php/temphum/week_values.php";
        ServerConnector sc = new ServerConnector(handler, url, "WEEK_STATS", 20000);
        sc.start();

        String [] data = {"0.0"};
        double [] temp = {0};
        double [] hum = {0};

        tempGraph.setAttr("temp", data, temp, false, color1, color2, color3, color4);
        humGraph.setAttr("hum", data, hum, false, color1, color2, color3, color4);


        return rootView;
    }
}
