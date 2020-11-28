package com.example.iotcontrol;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerConnector extends Thread {
    Handler handler;
    String myUrl;
    String name;
    int delay;

    volatile boolean isRunning = true;

    public ServerConnector(Handler handler, String myUrl, String name, int delay){
        this.handler = handler;
        this.myUrl = myUrl;
        this.name = name;
        this.delay = delay;
    }

    public void stopThread(){
        isRunning = false;
    }

    public void run(){
        while(isRunning){
            try{
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty ("Authorization", "Basic amVycnk6MTIzNA==");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(in);

                int result = bis.read();
                while(result != -1){
                    buf.write(result);
                    result = bis.read();
                }
                if(name.equals("DHT_VALUE")){
                    //Log.v(name, buf.toString());
                    processJsonResultDHT(buf.toString());
                }
                else if(name.equals("FAN_TOOGLE")){
                    Log.v(name, buf.toString());
                    processJsonFan(buf.toString());
                }
                else if(name.equals("WEEK_STATS")){
                    //Log.v(name, buf.toString());
                    processJsonWeek(buf.toString());
                }

                Thread.sleep(delay);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        if(!isRunning) return;
    }
    void processJsonResultDHT(String jsonString) throws JSONException, ParseException {
        JSONObject jObj = new JSONObject(jsonString);
        int success = jObj.getInt("success");

        if(success > 0){
            JSONArray jsonArray = jObj.getJSONArray("temphum");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);

                int id = o.getInt("id");
                long temperature = o.getLong("temperature");
                long humidity = o.getLong("humidity");

                String dayTime = o.getString("dayTime");

                Bundle b = new Bundle();
                b.putInt("id", id);
                b.putLong("temperature", temperature);
                b.putLong("humidity", humidity);
                b.putString("dayTime", dayTime);

                Message msg = handler.obtainMessage();
                msg.setData(b);
                msg.sendToTarget();

                //Log.v("FAN", id + "  " + temperature + " " + humidity + " " + dayTime);
            }
        }
        else{
            Bundle b = new Bundle();
            b.putInt("id", -1);
            b.putLong("temperature", -1);
            b.putLong("humidity", -1);
            b.putString("dayTime", "-1");

            Message msg = handler.obtainMessage();
            msg.setData(b);
            msg.sendToTarget();
        }
    }
    void processJsonFan(String jsonString) throws JSONException{
        JSONObject jObj = new JSONObject(jsonString);
        JSONArray jsonArray = jObj.getJSONArray("fan");

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject o = jsonArray.getJSONObject(i);

            int id = o.getInt("id");
            String powerStatus = o.getString("powerStatus");

            Bundle b = new Bundle();
            b.putInt("id", id);
            b.putString("powerStatus", powerStatus);

            Message msg = handler.obtainMessage();
            msg.setData(b);
            msg.sendToTarget();

            //Log.v("FAN", id + "  " + powerStatus);
        }
    }
    void processJsonWeek(String jsonString) throws JSONException {
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
