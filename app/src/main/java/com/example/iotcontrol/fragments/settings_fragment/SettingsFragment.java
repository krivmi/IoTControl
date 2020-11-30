package com.example.iotcontrol.fragments.settings_fragment;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsFragment extends Fragment {

    TextView about_text;
    Switch notify_switch;
    Switch night_switch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_settings, container, false);

        about_text = rootView.findViewById(R.id.about_text);
        notify_switch = rootView.findViewById(R.id.notification_switch);
        night_switch = rootView.findViewById(R.id.night_switch);

        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        SetSwitch(notify_switch, "notification", sh.getBoolean("notification", false));
        SetSwitch(night_switch, "night", sh.getBoolean("night", false));

        notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { StoreSwitch("notification", true); }
                else { StoreSwitch("notification", false); }
            }
        });

        night_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { StoreSwitch("night", true); }
                else { StoreSwitch("night", false); }
            }
        });

        return rootView;
    }
    public void SetSwitch(Switch s, String key, boolean value){
        if(value){
            s.setChecked(true);
            StoreSwitch(key, true);
        }
        else{
            s.setChecked(false);
            StoreSwitch(key, false);
        }
    }
    public void StoreSwitch(String key, boolean value){
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putBoolean(key, value);
        myEdit.commit();
    }
}