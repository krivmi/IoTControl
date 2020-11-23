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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;

public class TempHum extends AppCompatActivity {
    Handler handler = new Handler(){
        public void handleMessage(@NonNull Message msg) {

            txt4.setText(Html.fromHtml("Temperature: " + "<font color=black>" + String.valueOf(msg.getData().getLong("temperature") + " °C</font>")));
            txt5.setText(Html.fromHtml("Humidity: " + "<font color=black>" + String.valueOf(msg.getData().getLong("humidity") + " % </font>")));
            txt6.setText(Html.fromHtml("Last update: " + "<font color=black>" + String.valueOf(msg.getData().getString("dayTime") + "</font>")));

            drawTempHum(msg.getData().getLong("temperature"), 6.8F, 10, Color.BLACK, R.drawable.temp_large, imgTempLarge);
            drawTempHum(msg.getData().getLong("humidity"), 3.8F, -20, Color.rgb(29, 67, 144), R.drawable.humidity_large, imgHumLarge);
        }
    };
    public static Toolbar myBar;

    TextView txt4;
    TextView txt5;
    TextView txt6;
    TextView txt7;
    ImageView imgTempLarge;
    ImageView imgHumLarge;

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
        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        imgTempLarge.setImageResource(R.drawable.temp_large);
        imgHumLarge.setImageResource(R.drawable.humidity_large);

        ESPConnector esp = new ESPConnector(handler);
        esp.start();
    }
    public void drawTempHum(long value, float part, int jump, int color, int imagePath, ImageView i){         // vykresleni hodnoty do obrázku
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

}
