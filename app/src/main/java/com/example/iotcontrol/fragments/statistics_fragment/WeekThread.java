package com.example.iotcontrol.fragments.statistics_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class WeekThread extends Thread{
    Handler handler;

    public WeekThread(Handler handler){
        this.handler = handler;
    }

    public void run(){
        boolean isRunning = true;

        while(isRunning) {
            try {
                URL url = new URL("http://adelakrivankova.wz.cz/php/temphum/week_values.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Authorization", "Basic amVycnk6MTIzNA==");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(in);

                int result = bis.read();
                while (result != -1) {
                    buf.write(result);
                    result = bis.read();
                }
                Log.v("WEEK_VALUES", buf.toString());
                processJsonResult(buf.toString());

                //this.sleep(10 * 60 * 1000);
                this.sleep(20000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    void processJsonResult(String jsonString) throws JSONException {
        JSONObject jObj = new JSONObject(jsonString);
        int success = jObj.getInt("success");
        int count = jObj.getInt("count");

        String [] dayTimes = new String[count];
        double [] avg_temps = new double[count];
        double [] avg_hums = new double[count];

        if(success > 0){
            JSONArray jsonArray = jObj.getJSONArray("temphum");


            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);

                String dayTime = o.getString("dayTime");
                double avg_temp = o.getDouble("avg_temp");
                double avg_hum = o.getDouble("avg_hum");

                StringBuilder sb = new StringBuilder();
                sb.append(dayTime.substring(8));
                if(sb.charAt(0) == '0'){
                    sb.deleteCharAt(0);
                }
                sb.append(".");
                if(dayTime.substring(5, 7).charAt(0) == '0'){
                    sb.append(dayTime.substring(6, 7));
                }
                else{
                    sb.append(dayTime.substring(5, 7));
                }

                dayTimes[i] = sb.toString();
                avg_temps[i] = avg_temp;
                avg_hums[i] = avg_hum;

                //Log.v("FAN", id + "  " + temperature + " " + humidity + " " + dayTime);
            }
            Bundle b = new Bundle();
            b.putStringArray("dayTimes", dayTimes);
            b.putDoubleArray("avg_temps", avg_temps);
            b.putDoubleArray("avg_hums", avg_hums);

            Message msg = handler.obtainMessage();
            msg.setData(b);
            msg.sendToTarget();
        }
        else{
            Bundle b = new Bundle();
            dayTimes[0] = "0.0";
            avg_temps[0] = 0;
            avg_hums[0] = 0;

            b.putStringArray("dayTimes", dayTimes);
            b.putDoubleArray("avg_temps", avg_temps);
            b.putDoubleArray("avg_hums", avg_hums);

            Message msg = handler.obtainMessage();
            msg.setData(b);
            msg.sendToTarget();
        }
    }
}