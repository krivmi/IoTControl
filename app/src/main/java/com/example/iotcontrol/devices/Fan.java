package com.example.iotcontrol.devices;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.iotcontrol.R;
import com.example.iotcontrol.ServerConnector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.HttpURLConnection;
import java.net.URL;

public class Fan extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //int id = msg.getData().getInt("id");
            status = msg.getData().getString("powerStatus");

            compareStatus();
        }
    };
    boolean nightModeOn = false;
    Toolbar myBar;
    LinearLayout onoff;
    ImageView onoff_img;

    ServerConnector sc;

    String status = "off";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_fan);

        String url = "http://adelakrivankova.wz.cz/php/fan/read_value.php?id=1";
        sc = new ServerConnector(handler, url, "FAN_TOOGLE", 3000);
        sc.start();

        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);
        getSupportActionBar().setTitle("Rowenta fan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        myBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.stopThread();
                finish();
            }
        });

        onoff_img = findViewById(R.id.onoff_img);
        compareStatus();

        onoff = findViewById(R.id.onoff);
        onoff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == onoff){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:{
                            //promáčknutí změna pozadí
                            if(!nightModeOn){
                                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_effect) );
                            }
                            onoff.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:{
                            toogleFan(onoff_img);

                            Thread t = new Thread(new Runnable() {
                                public void run() {
                                    String url_str = "http://adelakrivankova.wz.cz/php/fan/update.php?id=1&powerStatus=" + status;
                                    try {
                                        URL url = new URL(url_str);
                                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                        urlConnection.setRequestProperty("Authorization", "Basic amVycnk6MTIzNA==");
                                        urlConnection.getInputStream();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                            });
                            t.start();
                            // změna trvalá po kliknutí
                            if(!nightModeOn){
                                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border) );
                            }

                            onoff.invalidate();
                            //necoo
                            break;
                        }
                    }
                }
                return true;
            }
        });

        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if(sh.getBoolean("night", false)){
            myBar.setBackgroundColor(getResources().getColor(R.color.colorLayout));
            myBar.setTitleTextColor(getResources().getColor(R.color.colorAqua));
            myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAqua), PorterDuff.Mode.SRC_ATOP);

            LinearLayout fan_layout = (LinearLayout) findViewById(R.id.fan_layout);
            fan_layout.setBackgroundColor(getResources().getColor(R.color.colorBlack));

            LinearLayout night = (LinearLayout) findViewById(R.id.night);
            LinearLayout turbo = (LinearLayout) findViewById(R.id.turbo);
            LinearLayout time = (LinearLayout) findViewById(R.id.time);
            LinearLayout desc = (LinearLayout) findViewById(R.id.desc);
            LinearLayout plus = (LinearLayout) findViewById(R.id.plus);
            View line_fan = (View) findViewById(R.id.line_fan);

            onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            night.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            turbo.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            time.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            desc.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            plus.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            line_fan.setBackgroundColor(getResources().getColor(R.color.colorAqua));

            nightModeOn = true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            sc.stopThread();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void compareStatus(){ //změna zapnutí nebo vypnutí podle aktuální situace
        if(status.equals("on")){
            if(!nightModeOn){
                onoff_img.getDrawable().setColorFilter(Color.rgb(29, 67, 144), PorterDuff.Mode.SRC_ATOP);
            }
            else{
                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark_on) );
            }
        }
        else if(status.equals("off")) {
            if(!nightModeOn){
                onoff_img.getDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }
            else{
                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            }
        }
    }

    public void toogleFan(ImageView i){
        if(status.equals("on")){
            if(!nightModeOn){
                i.getDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }
            else{
                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark) );
            }
            status = "off";
        }
        else if(status.equals("off")){
            if(!nightModeOn){
                i.getDrawable().setColorFilter(Color.rgb(29, 67, 144), PorterDuff.Mode.SRC_ATOP);
            }
            else{
                onoff.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_border_dark_on) );
            }
            status = "on";
        }
    }
}
