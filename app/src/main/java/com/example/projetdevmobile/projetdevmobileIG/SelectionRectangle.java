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

public class SelectionRectangle extends SurfaceView {

    private Paint paint;
    private ArrayList<Rect> selectedRects;
    private Rect rect;
    private ImageView viewOfImage;

    /**
     * Constructor
     * @param context
     * @param attrs
     */
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

    /**
     * Config the surfaceview
     * @param viewOfImage
     */
    public void configSurfaceView(ImageView viewOfImage){
        this.viewOfImage = viewOfImage;
        this.viewOfImage.setDrawingCacheEnabled(true);
        this.setX(viewOfImage.getX());
        this.setY(viewOfImage.getY());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewOfImage.getWidth(), viewOfImage.getHeight());
        this.setLayoutParams(layoutParams);
    }

    /**
     * Return selected rect
     * @return
     */
    public Rect saveSelected(){
        return rect;
    }

    /**
     * Setter of the rect
     * @param rect
     */
    public void setRect(Rect rect){
        this.rect = rect;
    }

    /**
     * Setter of selected rects corresponding to the accesses
     * @param rects
     */
    public void setSelectedRects(ArrayList<Rect> rects){
        this.selectedRects = rects;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // Draw accesses rects
        if(selectedRects != null){
            paint.setColor(Color.RED);
            for(Rect r : selectedRects) {
                if(r != null)
                    canvas.drawRect(r, paint);
                }
            }

        // Draw selection rect
        if(rect != null) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);
        }

        // Bring back surfaceview to front
        this.bringToFront();
    }
}
