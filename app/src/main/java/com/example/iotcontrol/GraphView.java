package com.example.iotcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class GraphView extends View {

    String type;
    String [] dateValues;
    float [] values;

    float originX = 0;
    float originY = 0;

    int cw = 0;
    int ch = 0;

    public GraphView(Context context){ super(context); }
    public GraphView(Context context, AttributeSet attrs) { super(context, attrs); }
    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    public void setOriginCoor(int offset){
        originX = offset;
        originY = ch - offset;
    }
    public void setAttr(String type, String [] dateValues, float [] values){
        this.type = type;
        this.dateValues = dateValues;
        this.values = values;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //String [] data = {"1.11", "2.11", "3.11", "4.11", "5.11", "6.11", "7.11", "8.11", "9.11", "10.11","11.11", "12.11"};
        //float [] temp = {2, 5, 10, 15, 20, 22, 24, 25, 26, 27, 28, 30};
        //float [] hum = {50, 65, 54, 56, 57, 60, 65, 55, 55, 54, 70, 90};
        int dataCount = dateValues.length;

        cw = canvas.getWidth();
        ch = canvas.getHeight();

        int offset = 60;

        setOriginCoor(offset);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK); //Color.rgb(0, 0, 0)
        paint.setStrokeWidth(5);

        //BORDER
        //paint.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(originX, originY,4, paint);
        canvas.drawLine(originX, originY, originX , offset, paint);
        canvas.drawLine(originX, originY, cw - offset , originY, paint);

        // DNY
        float partHor = (cw - offset - 10) / (dataCount + 1);

        ArrayList<Points> pointsHors = new ArrayList<Points>();

        for(int i = 1; i <= dataCount; i++){
            float dX = originX + (i * partHor);
            float dY = originY;

            pointsHors.add(new Points(dX, dY, values[i - 1]));
            paint.setColor(Color.BLUE);
            canvas.drawCircle(dX, dY,4, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(30);

            canvas.drawText(dateValues[i - 1], dX - 30, dY + 40, paint);
        }

        // TEPLOTA NEBO VLHKOST

        if(type == "temp"){
            int startJump = 20;
            int jump = 5;
            int jumpParts = 3;
            float partVer = (ch - offset * 4) / jumpParts;

            ArrayList<Points> pointsVer = new ArrayList<Points>();

            for(Integer i = 1, j = startJump; i <= jumpParts; i++, j += jump){
                float dX = originX;
                float dY = originY - (i * partVer);

                pointsVer.add(new Points(dX, dY, j));

                canvas.drawCircle(dX, dY,4, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(30);

                canvas.drawText(j.toString(), dX - 50, dY + 10, paint);
            }

            ArrayList<Points> dataPoints = new ArrayList<Points>();

            for(int i = 0; i < dataCount; i++){
                float koef;

                if(pointsHors.get(i).value < startJump){
                    koef = partVer / startJump;
                    koef *= pointsHors.get(i).value;
                }
                else{
                    koef = pointsHors.get(i).value - startJump;
                    koef *= (partVer/jump);
                    koef += partVer;
                }

                float dX = pointsHors.get(i).denX;
                float dY = pointsHors.get(i).denY - koef;

                dataPoints.add(new Points(dX, dY, values[i]));
                canvas.drawCircle(dX, dY,4, paint);
            }
            // DRAW LINES

            for(int i = 0; i < dataCount - 1; i++){
                canvas.drawLine(dataPoints.get(i).denX, dataPoints.get(i).denY, dataPoints.get(i + 1).denX, dataPoints.get(i + 1).denY , paint);
            }

        }
        else if(type == "hum"){
            int startJump = 40;
            int jump = 10;
            int jumpParts = 6;
            float partVer = (ch - offset * 4) / jumpParts;

            ArrayList<Points> pointsVer = new ArrayList<Points>();

            for(Integer i = 1, j = startJump; i <= jumpParts; i++, j += jump){
                float dX = originX;
                float dY = originY - (i * partVer);

                pointsVer.add(new Points(dX, dY, j));

                canvas.drawCircle(dX, dY,4, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(30);

                canvas.drawText(j.toString(), dX - 50, dY + 10, paint);
            }

            ArrayList<Points> dataPoints = new ArrayList<Points>();

            for(int i = 0; i < dataCount; i++){
                float koef;

                if(pointsHors.get(i).value < startJump){
                    koef = partVer / startJump;
                    koef *= pointsHors.get(i).value;
                }
                else{
                    koef = pointsHors.get(i).value - startJump;
                    koef *= (partVer/jump);
                    koef += partVer;
                }

                float dX = pointsHors.get(i).denX;
                float dY = pointsHors.get(i).denY - koef;

                dataPoints.add(new Points(dX, dY, values[i]));
                canvas.drawCircle(dX, dY,4, paint);
            }
            // DRAW LINES

            for(int i = 0; i < dataCount - 1; i++){
                canvas.drawLine(dataPoints.get(i).denX, dataPoints.get(i).denY, dataPoints.get(i + 1).denX, dataPoints.get(i + 1).denY , paint);
            }
        }

    }
}

class Points{
    public float denX;
    public float denY;
    public float value;

    public Points(float denX, float denY, float value){
        this.denX = denX;
        this.denY = denY;
        this.value = value;
    }
}
