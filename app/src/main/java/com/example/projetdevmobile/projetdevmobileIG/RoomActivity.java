package com.example.projetdevmobile.projetdevmobileIG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;
import com.example.projetdevmobile.tools.IdMaker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoomActivity extends AppCompatActivity implements SensorEventListener {
    private Habitation habitation;
    private Room room;

    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor sensorMagneto;


    private float[] floatGravity = new float[3];
    private final float[] floatOrientation = new float[3];
    private final float[] floatRotationMatrix = new float[9];

    private ActivityResultLauncher<Intent> launcher;
    private Orientation lastPictureOrientation;


    private EditText roomNameText;
    private ImageView imgNorth;
    private ImageView imgSouth;
    private ImageView imgEast;
    private ImageView imgWest;
    private ImageView imgCompass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneto = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        roomNameText = (EditText)findViewById(R.id.roomNameText);
        imgNorth = (ImageView)findViewById(R.id.imageNorth);
        imgSouth = (ImageView)findViewById(R.id.imageSouth);
        imgEast = (ImageView)findViewById(R.id.imageEast);
        imgWest = (ImageView)findViewById(R.id.imageWest);
        imgCompass = (ImageView)findViewById(R.id.imageCompass);

        launcherInit();

        Intent myIntent = getIntent();
        this.habitation = HabitationManager.getInstance().getHabitation(myIntent.getStringExtra("ObjectRecyclerParentName"));
        Boolean isCreation = myIntent.getBooleanExtra("isCreation", true);


        /*
        intent.putExtra("ObjectRecyclerName", resultDialogText);

        */

        if(!isCreation) {
            unFocusAndDisplay(myIntent);
        }
        else{
            focusAndCreate();
        }

        // Handle entry in EditText
        onKeyListenerRoomName();
        displayPictures();
    }


    /* UI SETUP */

    private void unFocusAndDisplay(Intent myIntent){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Hide keyboard
        roomNameText.clearFocus();

        String roomName = myIntent.getStringExtra("ObjectRecyclerName");
        this.room = (Room)habitation.getRoom(roomName);

        if (room != null) {
            roomNameText.setText(room.getName());
            displayPictures();
        }
    }

    private void focusAndCreate(){
        if(roomNameText.requestFocus()) { // request focus on EditText, display keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        room = new Room(getResources().getString(R.string.room), habitation);
        if(!habitation.availiableRoomName(room, getResources().getString(R.string.room)))
        {
            int i=1;
            while(!habitation.availiableRoomName(room, getResources().getString(R.string.room) + " " + i)){
                i++;
            }
            room.setName(getResources().getString(R.string.room) + " " + i);
        }

        roomNameText.setText(room.getName());
        habitation.addRoom(room);
    }

    private void onKeyListenerRoomName(){
        roomNameText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String roomName = roomNameText.getText().toString();

                    if(roomName.contentEquals("") || !habitation.availiableRoomName(room, roomName))
                    {
                        Toast.makeText(RoomActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        roomNameText.setText(room.getName());
                    }
                    else{
                        room.setName(roomName);
                        hideKeyboard(v);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void displayPictures(){
            //imgNorth.setImage(room.getPhoto(Orientation.NORTH).getImage());
    }

    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, sensorMagneto, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /* PICTURES */

    public void onClickNorth(android.view.View v){
        lastPictureOrientation = Orientation.NORTH;
        makeDecisionPicture();
    }

    public void onClickSouth(android.view.View v){
        lastPictureOrientation = Orientation.SOUTH;
        makeDecisionPicture();
    }

    public void onClickEast(android.view.View v){
        lastPictureOrientation = Orientation.EAST;
        makeDecisionPicture();
    }

    public void onClickWest(android.view.View v){
        lastPictureOrientation = Orientation.WEST;
        makeDecisionPicture();
    }

    private void makeDecisionPicture(){
        if (room.getPhoto(lastPictureOrientation) == null){
            takePicture();
        }
        else{
            Intent intent = new Intent(this, SelectActivity.class);
            intent.putExtra("ObjectRecyclerParentName", habitation.getName());
            intent.putExtra("ObjectRecyclerName", room.getName());
            intent.putExtra("Orientation", lastPictureOrientation.toString());
            startActivity(intent);
        }
    }

    private void launcherInit(){
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            saveImage(result);
                        }
                    }
                }
        );
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            launcher.launch(intent);
        }
    }

    private void saveImage(ActivityResult result){

        Bundle bundle = result.getData().getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");

        switch (lastPictureOrientation){
            case EAST:
                imgEast.setImageBitmap(bitmap);
                break;
            case WEST:
                imgWest.setImageBitmap(bitmap);
                break;
            case NORTH:
                imgNorth.setImageBitmap(bitmap);
                break;
            case SOUTH:
                imgSouth.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
        IdMaker idMaker = IdMaker.getInstance();
        String uniqueName = "image" + idMaker.getUIdImg() + ".data";
        try {
            FileOutputStream fos = null;

            fos = openFileOutput(uniqueName, MODE_PRIVATE);
            if(fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException r) {r.printStackTrace();}

        room.setPhoto(new Photo(lastPictureOrientation, uniqueName));
    }


    /* Compass */

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            floatGravity = sensorEvent.values;

        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] floatGeoMagnetic = sensorEvent.values;

            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

            imgCompass.setRotation((float) (-floatOrientation[0] * 180 / Math.PI));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);

    }
}