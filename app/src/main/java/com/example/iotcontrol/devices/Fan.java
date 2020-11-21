package com.example.iotcontrol.devices;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;

public class Fan extends AppCompatActivity {
    public static Toolbar myBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_fan);

        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);

        getSupportActionBar().setTitle("Rowenta fan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
    }

}
