package com.example.projetdevmobile.projetdevmobileIG;

import static android.graphics.Rect.intersects;

import static com.example.projetdevmobile.tools.Static.saveJson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.util.ArrayList;

public class VisitActivity extends AppCompatActivity {

    private VisitEntrances visitEntrances;
    private ImageView imageView;
    private View.OnTouchListener listenerImageView;

    private Habitation habitation;
    private Room currentRoom;
    private Photo currentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        imageView = (ImageView)findViewById(R.id.imageViewVisit);
        visitEntrances = (VisitEntrances) findViewById(R.id.visitEntrances);

        Intent myIntent = getIntent();
        this.habitation = HabitationManager.getInstance().getHabitation(myIntent.getStringExtra("HabitationName"));

        this.currentRoom = habitation.getRoomEntrance();
        this.currentPhoto = currentRoom.getPhoto(Orientation.NORTH);

        loadPhoto();

        listenerImageView = new VisitActivity.OnTouchEvent();
        imageView.setOnTouchListener(listenerImageView);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        visitEntrances.configSurfaceView(imageView);
    }

    /**
     * Display the corresoponding picture in the room of access depending on actual orientation
     * @param a
     */
    private void moveNextRoom(Access a)
    {
        currentPhoto = a.getRoom().getPhoto(currentPhoto.getOrientation());
        currentRoom = a.getRoom();
        loadPhoto();
    }

    /**
     * load photo to imageview
     */
    private void loadPhoto(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        imageView.setImageBitmap(currentPhoto.getImageBitmap(this));
        visitEntrances.setEntrances(currentPhoto.getAccess());
    }


    /**
     * On touch listener of the imageview
     */
    class OnTouchEvent implements ImageView.OnTouchListener {

        private float downX;
        private boolean swap = false;
        private int  MIN_DIST = 500;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (motionEvent.getPointerCount() == 1) {
                        boolean hitAccess = false;
                        for (Access a : currentPhoto.getAccess()) {
                            Rect r = a.getRect();

                            downX = motionEvent.getX(0);
                            float downY =  motionEvent.getY(0);

                            Point p1 = new Point((int)downX, (int)downY);
                            Point p2 = new Point(p1);
                            Rect point = new Rect(p1.x, p1.y, p2.x, p2.y);

                            if (r != null && intersects(r, point)) { // If pointed access, move to next room
                                moveNextRoom(a);
                                hitAccess = true;
                            }
                        }
                        if(!hitAccess){ // If not pointed access, then start potential swiping
                            swap = true;
                            downX = motionEvent.getX();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP: // Not swiping anymore
                    swap = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (motionEvent.getPointerCount() == 1) {
                        float deltaX = motionEvent.getX() - downX;
                        if (swap && Math.abs(deltaX) > MIN_DIST) { // If swiping successful
                            swap = false;
                            if (deltaX > 0) { // Swipe left to right
                                currentPhoto = currentRoom.getPhoto(Orientation.getLeft(currentPhoto.getOrientation()));
                                loadPhoto();
                                downX = motionEvent.getX();
                            } else { // Swipe right to left
                                currentPhoto = currentRoom.getPhoto(Orientation.getRight(currentPhoto.getOrientation()));
                                loadPhoto();
                                downX = motionEvent.getX();
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    }

    /**
     * When resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        saveJson(this);
    }
}