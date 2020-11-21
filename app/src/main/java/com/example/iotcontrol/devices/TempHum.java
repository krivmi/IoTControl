package com.example.iotcontrol.devices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.iotcontrol.ESPConnector;
import com.example.iotcontrol.R;

public class TempHum extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //txt2.setText("ID = " + String.valueOf(msg.getData().getInt("id")));
            //txt3.setText("PowerStatus = " + String.valueOf(msg.getData().getString("powerStatus")));

            //txt4.setText("ID: " + String.valueOf(msg.getData().getInt("id")));
            txt4.setText(Html.fromHtml("Temperature: " + "<font color=black>" + String.valueOf(msg.getData().getLong("temperature") + "</font>" + " °C")));
            txt5.setText(Html.fromHtml("Humidity: " + "<font color=black>" + String.valueOf(msg.getData().getLong("humidity") + "</font>" + " %")));
            txt6.setText(Html.fromHtml("Last update: " + "<font color=black>" + String.valueOf(msg.getData().getString("dayTime") + "</font>")));

            // vykresleni hodnoty do obrázku
            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
            myOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.temp_large,myOptions);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(mutableBitmap);
            int cw = canvas.getWidth();
            int ch = canvas.getHeight();

            int bX = bitmap.getWidth();
            int bY = bitmap.getHeight();

            int orX = 163;
            int orY = 500;
            float part = 6.8F;
            int height =(int) (part * (float) msg.getData().getLong("temperature") + part * 10);
            int width = 64;

            Rect rectangle = new Rect(orX, orY - height, width + orX, orY); //width, height, x, y

            canvas.drawRect(rectangle, paint);

            imgLarge.setAdjustViewBounds(true);
            imgLarge.setImageBitmap(mutableBitmap);

        }
    };
    public static Toolbar myBar;

    TextView txt4;
    TextView txt5;
    TextView txt6;
    TextView txt7;
    ImageView imgLarge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_temphum);

        txt4 = findViewById(R.id.textView4);
        txt5 = findViewById(R.id.textView5);
        txt6 = findViewById(R.id.textView6);
        txt7 = findViewById(R.id.textView7);
        imgLarge = findViewById(R.id.temperature_large);
        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);

        getSupportActionBar().setTitle("Thermo - Hydro meter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        imgLarge.setImageResource(R.drawable.temp_large);

        ESPConnector esp = new ESPConnector(handler);
        esp.start();
    }

}
