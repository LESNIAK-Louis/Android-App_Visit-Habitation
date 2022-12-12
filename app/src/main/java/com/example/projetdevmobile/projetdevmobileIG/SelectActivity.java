package com.example.projetdevmobile.projetdevmobileIG;

import static android.graphics.Rect.intersects;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    private SelectionRectangle selectionRectangle;
    private ImageView imageView;
    private View.OnTouchListener listenerImageView;
    private Rect rectSelection;
    private ArrayList<Rect> selectedRects;

    private Habitation habitation;
    private Room room;
    private int selectedRoom;
    private Photo photo;


    ArrayList<String> roomsName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        imageView = (ImageView)findViewById(R.id.imageViewSelect);
        selectionRectangle = (SelectionRectangle)findViewById(R.id.selectionRectangle);
        rectSelection = new Rect(0,0,0,0);
        selectedRects= new ArrayList<>();
        roomsName = new ArrayList<>();
        selectedRoom = 0;

        Intent myIntent = getIntent();
        this.habitation = HabitationManager.getInstance().getHabitation(myIntent.getStringExtra("ObjectRecyclerParentName"));
        String roomName = myIntent.getStringExtra("ObjectRecyclerName");
        this.room = (Room)habitation.getRoom(roomName);
        this.photo = room.getPhoto(getOrientation(myIntent.getStringExtra("Orientation")));

        getAccessRects();
        selectionRectangle.setSelectedRects(selectedRects);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = photo.getImageBitmap(this);
        imageView.setImageBitmap(bm);

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

    private void assignRoom(Rect rect)
    {
        if(habitation.getRooms().size() == 1) {
            selectionRectangle.setRect(null);
            selectionRectangle.invalidate();
        }
        else if(photo.checkIntersect(rect)){
            selectionRectangle.setRect(null);
            selectionRectangle.invalidate();
        }
        else {
            modifyAccess(null, rect);
        }
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

        rectSelection = new Rect(p1.x, p1.y, p2.x, p2.y);
    }

    private void modifyAccess(Access a, Rect rect){
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);
        builder.setTitle("Choisissez la pièce où mène la porte");
        roomsName.clear();
        for (ObjectRecycler r : habitation.getRooms()) {
            if(!room.getName().contentEquals(r.getName()))
                roomsName.add(r.getName());
        }

        int checkedItem = 0;
        // Modification
        if(a!=null) {
            checkedItem = roomsName.indexOf(a.getRoom().getName().toString());
        }
        builder.setSingleChoiceItems(roomsName.toArray(new String[roomsName.size()]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedRoom = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(a == null && rect != null) // Creation
                {
                    Rect newRect = new Rect();
                    newRect.set(rect);
                    newRect.sort();
                    photo.addAccess(new Access((Room) habitation.getRoom(roomsName.get(selectedRoom)), newRect));
                    selectedRects.add(newRect);
                    selectionRectangle.setRect(null);
                    selectionRectangle.invalidate();
                }
                 else { // Modification
                     a.setRoom((Room) habitation.getRoom(roomsName.get(selectedRoom)));
                    selectionRectangle.setRect(null);
                    selectionRectangle.invalidate();
                }
            }
        });
        if(a != null && rect == null) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedRects.remove(a.getRect());
                    photo.removeAccess(a);
                    selectionRectangle.setRect(null);
                    selectionRectangle.invalidate();
                }
            });
        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionRectangle.setRect(null);
                selectionRectangle.invalidate();
            }
        });

        builder.setCancelable(false);


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getAccessRects(){
        selectedRects.clear();
        for(Access a : photo.getAccess())
            selectedRects.add(a.getRect());
    }

    class OnTouchEvent implements ImageView.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (motionEvent.getPointerCount() == 1) {
                        for (Access a : photo.getAccess()){
                            Rect r = a.getRect();

                            Point p1 = new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0));
                            Point p2 = new Point(p1);
                            Rect point = new Rect(p1.x, p1.y, p2.x, p2.y);

                           if (r != null && intersects(r, point)) {
                                modifyAccess(a, null);
                            }
                        }
                        return true;
                    }
                    else if  (motionEvent.getPointerCount() == 2) {
                        modifyRect(motionEvent);
                        rectSelection.sort();
                        selectionRectangle.setRect(rectSelection);
                        selectionRectangle.invalidate();
                        return true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (motionEvent.getPointerCount() == 2) {
                        modifyRect(motionEvent);
                        rectSelection.sort();
                        selectionRectangle.setRect(rectSelection);
                        selectionRectangle.invalidate();
                        return true;
                    }
                case MotionEvent.ACTION_POINTER_UP:
                    if  (motionEvent.getPointerCount() == 2) {
                        assignRoom(selectionRectangle.saveSelected());
                        return true;
                    }
            }
            return false;
        }
    }
}