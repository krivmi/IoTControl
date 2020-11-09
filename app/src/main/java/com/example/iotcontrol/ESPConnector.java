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

public class ESPConnector extends Thread {
    Handler handler;

    public ESPConnector(Handler handler){
        this.handler = handler;
    }
    public void run(){

        boolean isRunning = true;

        while(isRunning){
            try{
                //URL url = new URL("http://adelakrivankova.wz.cz/php/fan/read_value.php?id=1");
                URL url = new URL("http://adelakrivankova.wz.cz/php/temphum/last_value.php");
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
                Log.v("TempHum", buf.toString());
                //processJsonResult(buf.toString());
                processJsonResultTemphum(buf.toString());

                Thread.sleep(2000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    void processJsonResultTemphum(String jsonString) throws JSONException, ParseException {
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

    void processJsonResult(String jsonString) throws JSONException{
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
}
