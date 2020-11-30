package com.example.iotcontrol.devices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.iotcontrol.R;
import com.example.iotcontrol.ServerConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TempHum extends AppCompatActivity {
    Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void handleMessage(@NonNull Message msg) {

            txt4.setText(Html.fromHtml("Temperature: " + "<font color=black>" + String.valueOf(msg.getData().getLong("temperature") + " °C</font>")));
            txt5.setText(Html.fromHtml("Humidity: " + "<font color=black>" + String.valueOf(msg.getData().getLong("humidity") + " % </font>")));
            txt6.setText(Html.fromHtml("Last update: " + "<font color=black>" + String.valueOf(msg.getData().getString("dayTime") + "</font>")));

            drawTempHum(msg.getData().getLong("temperature"), 6.8F, 10, Color.BLACK, R.drawable.temp_large, imgTempLarge);
            drawTempHum(msg.getData().getLong("humidity"), 3.8F, -20, Color.rgb(29, 67, 144), R.drawable.humidity_large, imgHumLarge);
        }
    };
    Toolbar myBar;
    TextView txt4;
    TextView txt5;
    TextView txt6;
    ImageView imgTempLarge;
    ImageView imgHumLarge;

    String date;
    ServerConnector sc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_temphum);

        txt4 = findViewById(R.id.textView4);
        txt5 = findViewById(R.id.textView5);
        txt6 = findViewById(R.id.textView6);
        imgTempLarge = findViewById(R.id.temperature_large);
        imgHumLarge = findViewById(R.id.humidityLarge);
        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);

        getSupportActionBar().setTitle("Thermo - Hydro meter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.stopThread();
                finish();
            }
        });

        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        imgTempLarge.setImageResource(R.drawable.temp_large);
        imgHumLarge.setImageResource(R.drawable.humidity_large);

        String url = "http://adelakrivankova.wz.cz/php/temphum/last_value.php";
        sc = new ServerConnector(handler, url, "DHT_VALUE", 4000);
        sc.start();
    }
    private void drawTempHum(long value, float part, int jump, int color, int imagePath, ImageView i){         // vykresleni hodnoty do obrázku
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imagePath, myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);

        int orX = 163;
        int orY = 500;

        int height = (int) (part * (float) value + part * jump);
        int width = 64;

        Rect rectangle = new Rect(orX, orY - height, width + orX, orY); //width, height, x, y

        canvas.drawRect(rectangle, paint);

        i.setAdjustViewBounds(true);
        i.setImageBitmap(mutableBitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private boolean isOn(){

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy/M/dd hh:mm:ss");
        try {
            Date date1 = sdf2.parse(date);
            Date date2 = Calendar.getInstance().getTime();

            long [] result = getDifference(date1, date2);

            if(result[0] < 1 && result[1] < 1 && result[2] < 5){
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public long [] getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.v("LOG", "startDate : " + startDate);
        Log.v("LOG","endDate : "+ endDate);
        Log.v("LOG","different : " + different);

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

        Log.v("TAG", elapsedDays + " " +elapsedHours+ " " + elapsedMinutes+ " " +elapsedSeconds);
        long [] result = {elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
        return result;
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


}
