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

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SelectionRectangle extends SurfaceView {

    private Paint paint;
    private ArrayList<Rect> selectedRects;
    private Rect rect;
    private ImageView viewOfImage;

    public SelectionRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.setZOrderOnTop(true);
        this.setWillNotDraw(false);

        paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(5);
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void configSurfaceView(ImageView viewOfImage){
        this.viewOfImage = viewOfImage;
        this.viewOfImage.setDrawingCacheEnabled(true);
        this.setX(viewOfImage.getX());
        this.setY(viewOfImage.getY());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewOfImage.getWidth(), viewOfImage.getHeight());
        this.setLayoutParams(layoutParams);
    }

    public Rect saveSelected(){
        return rect;
    }

    public void setRect(Rect rect){
        this.rect = rect;
    }

    public void setSelectedRects(ArrayList<Rect> rect){
        this.selectedRects = rect;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(selectedRects != null){
            paint.setColor(Color.RED);
            for(Rect r : selectedRects) {
                if(r != null){
                    canvas.drawRect(r, paint);
                }
            }
        }

        if(rect != null) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);
        }

        this.bringToFront();
    }
}
