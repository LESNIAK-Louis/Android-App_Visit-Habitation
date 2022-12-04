package com.example.projetdevmobile.projetdevmobileIG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

public class SelectActivity extends AppCompatActivity {

    private SelectionRectangle selectionRectangle;
    private ImageView imageView;
    private View.OnTouchListener listenerImageView;
    private Rect rect;

    private Habitation habitation;
    private Room room;
    private Photo photo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        imageView = (ImageView)findViewById(R.id.imageViewSelect);
        selectionRectangle = (SelectionRectangle)findViewById(R.id.selectionRectangle);
        rect = new Rect(0,0,0,0);

        Intent myIntent = getIntent();
        this.habitation = HabitationManager.getInstance().getHabitation(myIntent.getStringExtra("ObjectRecyclerParentName"));
        String roomName = myIntent.getStringExtra("ObjectRecyclerName");
        this.room = (Room)habitation.getRoom(roomName);
        this.photo = room.getPhoto(getOrientation(myIntent.getStringExtra("Orientation")));

        Bitmap bm = photo.getImageBitmap(this.getFilesDir());
        imageView.setImageBitmap(bm);
        imageView.setMinimumWidth(bm.getWidth());
        imageView.setMinimumHeight(bm.getHeight());


        listenerImageView = new OnTouchEvent();
        imageView.setOnTouchListener(listenerImageView);
    }

    private Orientation getOrientation(String o){
        Orientation orientation;
        if(o.contentEquals("North"))
            orientation = Orientation.NORTH;
        else if (o.contentEquals("South"))
            orientation = Orientation.SOUTH;
        else if (o.contentEquals("West"))
            orientation = Orientation.WEST;
        else
            orientation = Orientation.EAST;

        return orientation;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        selectionRectangle.configSurfaceView(imageView);
    }

    private void showImageDialog(Bitmap bm) {
        ImageView imageViewCrop = new ImageView(this);
        imageViewCrop.setImageBitmap(bm);

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                selectionRectangle.setRect(null);
                selectionRectangle.invalidate();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.addContentView(imageViewCrop, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
    }

    private void checkBounds(Point p){
        if(p.x < 0) p.x = 0;
        if(p.x > imageView.getWidth()) p.x = imageView.getWidth();

        if(p.y < 0) p.y = 0;
        if(p.y >  imageView.getHeight()) p.y =  imageView.getHeight();
    }


    private void modifyRect(MotionEvent motionEvent){
        Point p1 = new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0));
        Point p2 = new Point((int) motionEvent.getX(motionEvent.getPointerCount() - 1), (int) motionEvent.getY(motionEvent.getPointerCount() - 1));

        checkBounds(p1);
        checkBounds(p2);

        rect = new Rect(p1.x, p1.y, p2.x, p2.y);
    }

    class OnTouchEvent implements ImageView.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_POINTER_UP:
                        showImageDialog(selectionRectangle.saveSelected());
                default:
                    if (motionEvent.getPointerCount() == 2) {
                        modifyRect(motionEvent);
                        rect.sort();
                        selectionRectangle.setRect(rect);
                        selectionRectangle.invalidate();
                    }
            }
            return true;
        }
    }
}