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

public class ESPConnector extends Thread {
    Handler handler;

    public ESPConnector(Handler handler){
        this.handler = handler;
    }
    public void run(){

        boolean isRunning = true;

        while(isRunning){
            try{
                URL url = new URL("http://adelakrivankova.wz.cz/php/read_all.php?id=1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(in);

                int result = bis.read();
                while(result != -1){
                    buf.write(result);
                    result = bis.read();
                }
                //Log.v("FanStatus", buf.toString());
                processJsonResult(buf.toString());

                Thread.sleep(2000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
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
