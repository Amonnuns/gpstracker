package br.unifacs.gpstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import javax.annotation.Nullable;
import androidx.annotation.NonNull;

public class GNSSView extends View {

    private static GnssStatus newStatus;
    private int r;
    private int height, width;
    private static int buttonValue;

    public GNSSView(Context context, @Nullable AttributeSet attrs){

        super(context, attrs);
    }



    public void onSatelliteStatusChanged(GnssStatus status) {
        newStatus=status;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        if (width<height)
            r=(int)(width/2*0.9);
        else
            r=(int)(height/2*0.9);

        Paint circlePain = new Paint();
        circlePain.setStyle(Paint.Style.STROKE);
        circlePain.setColor(Color.argb(255, 255, 0, 0));


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.argb(60, 255, 255, 255));


        Paint painSats = new Paint();
        painSats.setStyle(Paint.Style.STROKE);
        painSats.setColor(Color.argb(255, 255, 0, 0));


        Paint dashPaint = new Paint();
        dashPaint.setARGB(150, 0, 255, 0);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        int radius=r;
        canvas.drawCircle(computeXc(0), computeYc(0), radius+40, paint);
        radius=(int)(radius*Math.cos(Math.toRadians(45)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius+40, paint);
        radius=(int)(radius*Math.cos(Math.toRadians(60)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius+40, paint);

        canvas.drawLine(computeXc(0),computeYc(-r-40),computeXc(0),computeYc(r+40),dashPaint);
        canvas.drawLine(computeXc(-r-40),computeYc(0),computeXc(r+40),computeYc(0),dashPaint);
        radius=(int)(r*Math.cos(Math.toRadians(30)));
        canvas.drawLine(computeXc(-r),computeYc(-radius),computeXc(r),computeYc(radius),dashPaint);
        radius=(int)(r*Math.cos(Math.toRadians(180)));
        canvas.drawLine(computeXc(-r),computeYc(-radius),computeXc(r),computeYc(radius),dashPaint);


        // desenhando os satélites
        if (newStatus!=null) {
            for(int i=0;i<newStatus.getSatelliteCount();i++) {

                switch (buttonValue){
                    case 0:
                        break;
                    case GnssStatus.CONSTELLATION_GPS:
                        if (newStatus.getConstellationType(i) != GnssStatus.CONSTELLATION_GPS)
                            continue;
                        break;
                    case GnssStatus.CONSTELLATION_BEIDOU:
                        if (newStatus.getConstellationType(i) != GnssStatus.CONSTELLATION_BEIDOU)
                            continue;
                        break;
                    case GnssStatus.CONSTELLATION_GLONASS:
                        if (newStatus.getConstellationType(i) != GnssStatus.CONSTELLATION_GLONASS)
                            continue;
                        break;
                    case GnssStatus.CONSTELLATION_GALILEO:
                        if (newStatus.getConstellationType(i) != GnssStatus.CONSTELLATION_GALILEO)
                            continue;
                        break;
                }

                float az=newStatus.getAzimuthDegrees(i);
                float el=newStatus.getElevationDegrees(i);
                float x=(float)(r*Math.cos(Math.toRadians(el))*Math.sin(Math.toRadians(az)));
                float y=(float)(r*Math.cos(Math.toRadians(el))*Math.cos(Math.toRadians(az)));
                painSats.setTextAlign(Paint.Align.CENTER);
                painSats.setTextSize(50);


                circlePain.setTextAlign(Paint.Align.CENTER);
                circlePain.setTextSize(35);
                circlePain.getTextAlign();

                circlePain = setCircleCollor(circlePain,newStatus.getConstellationType(i));
                canvas.drawCircle(computeXc(x)-1, computeYc(y)-10, 30, circlePain);
                String satID=newStatus.getSvid(i)+"";
                canvas.drawText(satID, computeXc(x), computeYc(y), circlePain);
            }
        }
    }

    private int computeXc(double x) {
        return (int)(x+width/2);
    }
    private int computeYc(double y) {
        return (int)(-y+height/2);
    }

    private Paint setCircleCollor(Paint circle, int id){

        switch (id){
            case GnssStatus.CONSTELLATION_GPS:
                circle.setColor(Color.argb(255,255,255,0)); break;
            case GnssStatus.CONSTELLATION_BEIDOU:
                circle.setColor(Color.argb(255,0,255,255)); break;
            case GnssStatus.CONSTELLATION_GLONASS:
                circle.setColor(Color.argb(255,255,0,0)); break;
            case GnssStatus.CONSTELLATION_GALILEO:
                circle.setColor(Color.argb(255,255,0,255)); break;
        }

        return circle;
    }

    public void setButtonValue(int value){
        buttonValue = value;

    }

}