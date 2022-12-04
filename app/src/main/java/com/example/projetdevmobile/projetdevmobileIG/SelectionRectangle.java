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
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectionRectangle extends SurfaceView {

    private Paint paint;
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
        this.setX(viewOfImage.getX());
        this.setY(viewOfImage.getY());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewOfImage.getWidth(), viewOfImage.getHeight());
        this.setLayoutParams(layoutParams);
    }

    public Bitmap saveSelected(){
        viewOfImage.setDrawingCacheEnabled(true);
        Bitmap bmap = viewOfImage.getDrawingCache();
        return Bitmap.createBitmap(bmap, rect.left,rect.top,rect.right-rect.left, rect.bottom-rect.top);
    }

    public void setRect(Rect rect){
        this.rect = rect;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(rect != null) {
            canvas.drawRect(rect, paint);
            this.bringToFront();
        }
    }
}
