package com.example.projetdevmobile.projetdevmobileIG;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projetdevmobile.projetdevmobile.Access;

import java.util.ArrayList;

public class VisitEntrances extends SurfaceView {

    private Paint paint;
    private ArrayList<Access> accesses;
    private ImageView viewOfImage;
    private static int textSize = 52;

    public VisitEntrances(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.setZOrderOnTop(true);
        this.setWillNotDraw(false);

        paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(5);
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setTextSize(textSize);
    }

    public void configSurfaceView(ImageView viewOfImage){
        this.viewOfImage = viewOfImage;
        this.viewOfImage.setDrawingCacheEnabled(true);
        this.setX(viewOfImage.getX());
        this.setY(viewOfImage.getY());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewOfImage.getWidth(), viewOfImage.getHeight());
        this.setLayoutParams(layoutParams);
    }

    /**
     * Setter of the accesses
     * @param accesses
     */
    public void setEntrances(ArrayList<Access> accesses){
        this.accesses = accesses;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(accesses != null){
            for(Access a : accesses) {
                if(a != null){
                    // Access rect
                    Rect rect = a.getRect();
                    paint.setColor(Color.RED);
                    canvas.drawRect(rect, paint);

                    // Room name
                    paint.setColor(Color.GREEN);
                    canvas.drawText(a.getRoom().getName(),rect.left + (rect.right - rect.left)/2 - textSize/2 ,rect.top + (rect.bottom - rect.top)/2 - textSize/2, paint);
                }
            }
        }


        this.bringToFront();
    }
}
