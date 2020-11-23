package com.example.iotcontrol.devices;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;

public class Fan extends AppCompatActivity {
    public static Toolbar myBar;

    ImageView onoff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_fan);

        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);

        getSupportActionBar().setTitle("Rowenta fan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        onoff = findViewById(R.id.onoff);

        onoff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == onoff){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:{
                            v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fan_image_click));

                            onoff.getDrawable().setColorFilter(Color.rgb(29, 67, 144), PorterDuff.Mode.SRC_ATOP);
                            onoff.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:{
                            onoff.getDrawable().clearColorFilter();
                            onoff.invalidate();

                            //necoo
                            break;
                        }
                    }
                }
                return true;
            }
        });

    }

}
