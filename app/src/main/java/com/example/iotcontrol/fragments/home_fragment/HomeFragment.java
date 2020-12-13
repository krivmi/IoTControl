package com.example.iotcontrol.fragments.home_fragment;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.App;
import com.example.iotcontrol.R;
import com.example.iotcontrol.ServerConnector;
import com.example.iotcontrol.devices.Fan;
import com.example.iotcontrol.devices.TempHum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private static Context context;

    private NotificationManagerCompat notificationManager;

    LinearLayout fragment_home;
    ListView itemListView;
    ItemsAdapter adapter;
    ArrayList<Item> arrayList;

    String defaultDate = "2020/01/01 00:00:00";
    boolean wasChecked = false;

    Handler handler_fan = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            arrayList.get(0).status = msg.getData().getString("powerStatus");
            adapter.notifyDataSetChanged();
            //Log.v("Thread running", String.valueOf(Thread.activeCount()));
        }
    };
    Handler handler_dht = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            long actHum = msg.getData().getLong("humidity");
            String actDayTime = msg.getData().getString("dayTime");

            String start_date = actDayTime.replace("-", "/");
            long [] difference = dateCompare(start_date, getCurrentDateFormatted());

            if(difference[0] < 1 && difference[1] < 1 && difference[2] < 1 && difference[3] < 60){ arrayList.get(1).status = "ON"; }
            else{ arrayList.get(1).status = "OFF"; }
            adapter.notifyDataSetChanged();
            //Toast.makeText(getContext(), difference[0] + " " + difference[1]+ " " +  difference[2]+ " " +  difference[3], Toast.LENGTH_SHORT).show();

            SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            boolean notifyMe = sh.getBoolean("notification", true);

            //Log.v("NOTIFY:",   String.valueOf(notifyMe));

            long[] difference2 = dateCompare(sh.getString("lastNotificationDate", defaultDate), getCurrentDateFormatted());
            if (difference2[0] >= 0 && difference2[1] >= 0 && difference2[2] >= 5 && difference2[3] >= 0) { // po 5 minutách dojde znovu notifikace, pokud je přesáhnuta hranice vlhkosti
                wasChecked = false;
            } else {
                wasChecked = true;
            }
            //Toast.makeText(getContext(), difference2[0] + " " + difference2[1]+ " " +  difference2[2]+ " " +  difference2[3], Toast.LENGTH_SHORT).show();

            if(notifyMe) {
                if (!wasChecked && arrayList.get(1).status.equals("ON")) { //notifikace dojde jen pokud je aktuálně zařízení připojené, snímá vlhkost a je dodržen 5 minutový odstup od předchozí notifikace
                    if (actHum > 65) {
                        String title = "Humidity";
                        String message = "Humidity in your room exceeded 65 %, we recommend you to open the window.";
                        sendOnChannel1(title, message);
                    } else if (actHum < 40) {
                        String title = "Humidity";
                        String message = "Humidity in your room is below 40 %, we recommend you to increase it.";
                        sendOnChannel1(title, message);
                    }
                    String lastNotificationDate = getCurrentDateFormatted();
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("lastNotificationDate", lastNotificationDate);
                    myEdit.commit();

                    wasChecked = true;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();

        String url = "http://adelakrivankova.wz.cz/php/fan/read_value.php?id=1";
        ServerConnector t_fan = new ServerConnector(handler_fan, url, "FAN_TOOGLE", 4000);
        t_fan.start();

        String url2 = "http://adelakrivankova.wz.cz/php/temphum/last_value.php";
        ServerConnector t_dht = new ServerConnector(handler_dht, url2, "DHT_VALUE", 4000);
        t_dht.start();

        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean nightModeOn = sh.getBoolean("night", false);

        int imgRes [] = {R.drawable.fan_icon, R.drawable.thermometer_icon};

        if(nightModeOn){
            fragment_home = rootView.findViewById(R.id.fragment_home);
            fragment_home.setBackgroundColor(getResources().getColor(R.color.colorBlack));

            imgRes[0] = R.drawable.fan_icon_dark;
            imgRes[1] = R.drawable.thermometer_icon_dark;
        }

        int ids [] = {0, 1};
        String texts [] = {"Rowenta fan", "DHT11 Thermo - hydro meter"};
        String statuses [] = {"OFF", "OFF"};


        arrayList = new ArrayList<Item>();
        for(int i = 0; i < ids.length; i++){
            arrayList.add(new Item(ids[i], texts[i], statuses[i], imgRes[i]));
        }
        adapter = new ItemsAdapter(getContext(), arrayList);
        itemListView = rootView.findViewById(R.id.listView1);
        itemListView.setAdapter(adapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item myItem = (Item) itemListView.getItemAtPosition(position);

                //Toast.makeText(getActivity().getApplicationContext(), myItem.name, Toast.LENGTH_SHORT).show();

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

        notificationManager = NotificationManagerCompat.from(getContext());

        return rootView;
    }
    private boolean dhtIsOn(String start_date){
        start_date = start_date.replace("-", "/");
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        try {
            Date date1 = sdf.parse(start_date);
            Date date2 = sdf.parse(getCurrentDateFormatted());

            long [] result = getDifference(date1, date2);

            if(result[0] < 1 && result[1] < 1 && result[2] < 1 && result[3] < 30){
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    private long[] dateCompare(String start_date, String stopDate){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long [] result = new long[4];
        try {
            Date date1 = sdf.parse(start_date);
            Date date2 = sdf.parse(getCurrentDateFormatted());

            result = getDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String getCurrentDateFormatted(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //cal.add(Calendar.HOUR_OF_DAY, 1);

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return sdf.format(cal.getTime());
    }
    public long [] getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        //Log.v("LOG", "startDate : " + startDate);
        //Log.v("LOG","endDate : "+ endDate);
        //Log.v("LOG","different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        //Log.v("TAG", elapsedDays + " " +elapsedHours+ " " + elapsedMinutes+ " " +elapsedSeconds);
        long [] result = {Math.abs(elapsedDays), Math.abs(elapsedHours), Math.abs(elapsedMinutes), Math.abs(elapsedSeconds)};
        return result;
    }
    public void sendOnChannel1(String title, String message){
        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[] { 400, 400 })
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();

        notificationManager.notify(1, notification);
    }
}