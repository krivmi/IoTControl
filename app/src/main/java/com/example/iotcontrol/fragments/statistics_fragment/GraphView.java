package com.example.iotcontrol.fragments.statistics_fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    public void setAttr(String type, String [] dateValues, double [] values, boolean innvalidate){
        this.type = type;
        this.dateValues = dateValues;

        float [] arr = new float[values.length];
        for(int i = 0; i < arr.length; i++) {
             arr[i] = (float) values[i];
        }
        this.values = arr;

        if(innvalidate){
            invalidate();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    public Paint setPaint(int color, Paint.Style style, int strokeWidth){
        Paint i = new Paint();
        i.setColor(color);
        i.setStrokeWidth(strokeWidth);
        i.setStyle(style);
        return i;
    }
    public Paint setPaint(int color, Paint.Style style, int strokeWidth, int textSize){
        Paint i = new Paint();
        i.setColor(color);
        i.setStrokeWidth(strokeWidth);
        i.setStyle(style);
        i.setTextSize(textSize);
        return i;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int dataCount = dateValues.length;

        cw = canvas.getWidth();
        ch = canvas.getHeight();

        int offset = 60;
        int dotSize = 8;

        setOriginCoor(offset);

        Paint p_lines = setPaint(Color.BLACK, Paint.Style.FILL, 8);
        Paint p_points = setPaint(Color.rgb( 45, 114, 143), Paint.Style.FILL, 8);
        Paint p_text = setPaint(Color.BLACK, Paint.Style.FILL, 8, 35);
        Paint p_lines_dots = setPaint(Color.rgb(59, 142, 165), Paint.Style.FILL, 6);

        // BORDER
        //paint.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        // XY LINES
        canvas.drawLine(originX, originY, originX , offset, p_lines);
        canvas.drawLine(originX, originY, cw - offset , originY, p_lines);

        // ORIGIN
        canvas.drawCircle(originX, originY,dotSize, p_points);

        // DNY - KOLECKA A TEXT
        float partHor = (cw - offset - 10) / (dataCount + 1);

        ArrayList<Points> pointsHors = new ArrayList<Points>();

        for(int i = 1; i <= dataCount; i++){
            float dX = originX + (i * partHor);
            float dY = originY;

            pointsHors.add(new Points(dX, dY, values[i - 1]));
            canvas.drawCircle(dX, dY,dotSize, p_points);
            canvas.drawText(dateValues[i - 1], dX - 43, dY + 45, p_text);
        }

        // TEPLOTA NEBO VLHKOST

        if(type == "temp"){
            int startJump = 20;
            int jump = 5;
            int jumpParts = 3;
            float partVer = (ch - offset * 4) / jumpParts;

            ArrayList<Points> pointsVer = new ArrayList<Points>();
            // TEPLOTA - KOLECKA A TEXT
            for(Integer i = 1, j = startJump; i <= jumpParts; i++, j += jump){
                float dX = originX;
                float dY = originY - (i * partVer);

                pointsVer.add(new Points(dX, dY, j));

                canvas.drawCircle(dX, dY,dotSize, p_points);
                canvas.drawText(j.toString(), dX - 55, dY + 10, p_text);
            }

            ArrayList<Points> dataPoints = new ArrayList<Points>();
            // KOLECKA - BODY V GRAFU
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
                canvas.drawCircle(dX, dY,dotSize, p_points);
            }
            // USECKY MEZI BODY
            for(int i = 0; i < dataCount - 1; i++){
                canvas.drawLine(dataPoints.get(i).denX, dataPoints.get(i).denY, dataPoints.get(i + 1).denX, dataPoints.get(i + 1).denY , p_lines_dots);
            }
            // ZNOVU BODY
            for(int i = 0; i < dataCount; i++){
                float dX = dataPoints.get(i).denX;
                float dY = dataPoints.get(i).denY;
                canvas.drawCircle(dX, dY,dotSize, p_points);
            }

        }
        else if(type == "hum"){
            int startJump = 40;
            int jump = 10;
            int jumpParts = 6;
            float partVer = (ch - offset * 4) / jumpParts;

            ArrayList<Points> pointsVer = new ArrayList<Points>();
            // VLHKOST - KOLECKA A TEXT
            for(Integer i = 1, j = startJump; i <= jumpParts; i++, j += jump){
                float dX = originX;
                float dY = originY - (i * partVer);

                pointsVer.add(new Points(dX, dY, j));

                canvas.drawCircle(dX, dY,dotSize, p_points);
                canvas.drawText(j.toString(), dX - 55, dY + 10, p_text);
            }

            ArrayList<Points> dataPoints = new ArrayList<Points>();
            // KOLECKA - BODY V GRAFU
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
                canvas.drawCircle(dX, dY,dotSize, p_points);
            }
            // USECKY MEZI BODY
            for(int i = 0; i < dataCount - 1; i++){
                canvas.drawLine(dataPoints.get(i).denX, dataPoints.get(i).denY, dataPoints.get(i + 1).denX, dataPoints.get(i + 1).denY , p_lines_dots);
            }
            // ZNOVU BODY
            for(int i = 0; i < dataCount; i++){
                float dX = dataPoints.get(i).denX;
                float dY = dataPoints.get(i).denY;
                canvas.drawCircle(dX, dY,dotSize, p_points);
            }

        }

    }
}

