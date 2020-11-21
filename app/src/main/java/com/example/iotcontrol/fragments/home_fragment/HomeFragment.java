package com.example.iotcontrol.fragments.home_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;
import com.example.iotcontrol.devices.Fan;
import com.example.iotcontrol.devices.TempHum;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView itemListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        int ids [] = {0, 1};
        String texts [] = {"Rowenta fan", "DHT11 Thermo - hydro meter"};
        String statuses [] = {"OFF", "OFF"};
        int imgRes [] = {R.drawable.fan_icon, R.drawable.thermometer_icon};

        ArrayList<Item> arrayList = new ArrayList<Item>();
        for(int i = 0; i < ids.length; i++){
            arrayList.add(new Item(ids[i], texts[i], statuses[i], imgRes[i]));
        }
        ItemsAdapter adapter = new ItemsAdapter(getContext(), arrayList);
        // Attach the adapter to a ListView
        itemListView = rootView.findViewById(R.id.listView1);
        itemListView.setAdapter(adapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item myItem = (Item) itemListView.getItemAtPosition(position);

                Toast.makeText(getActivity().getApplicationContext(), myItem.name, Toast.LENGTH_SHORT).show();

                if(myItem.id == 0){
                    Intent intent = new Intent(getActivity().getApplicationContext(), Fan.class);
                    startActivity(intent);
                }
                else if(myItem.id == 1){
                    Intent intent = new Intent(getActivity().getApplicationContext(), TempHum.class);
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }
}