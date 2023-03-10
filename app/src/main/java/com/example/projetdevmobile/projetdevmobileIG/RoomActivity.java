package com.example.projetdevmobile.projetdevmobileIG;

import static com.example.projetdevmobile.tools.Static.saveJson;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projetdevmobile.BuildConfig;
import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;
import com.example.projetdevmobile.tools.IdMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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

    private Uri currPicturePath;

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

    /**
     * Unfocus EditText and display informations
     */
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

    /**
     * Focus EditText and create the room
     */
    private void focusAndCreate(){
        if(roomNameText.requestFocus()) { // request focus on EditText, display keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        room = new Room(getResources().getString(R.string.room), habitation.getName());
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

    /**
     * onKeyListener for the EditText
     */
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

    /**
     * display all pictures associated with the room if existing
     */
    private void displayPictures(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        if(room.getPhoto(Orientation.NORTH) != null)
            imgNorth.setImageBitmap(room.getPhoto(Orientation.NORTH).getImageBitmap(this));

        if(room.getPhoto(Orientation.EAST) != null)
            imgEast.setImageBitmap(room.getPhoto(Orientation.EAST).getImageBitmap(this));

        if(room.getPhoto(Orientation.WEST) != null)
            imgWest.setImageBitmap(room.getPhoto(Orientation.WEST).getImageBitmap(this));

        if(room.getPhoto(Orientation.SOUTH) != null)
            imgSouth.setImageBitmap(room.getPhoto(Orientation.SOUTH).getImageBitmap(this));
    }

    /**
     * Hide keyboard
     * @param v
     */
    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    /**
     * When resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, sensorMagneto, SensorManager.SENSOR_DELAY_NORMAL);
        displayPictures();
        saveJson(this);
    }

    /* PICTURES */

    /**
     * OnClick picture North
     * @param v
     */
    public void onClickNorth(android.view.View v){
        lastPictureOrientation = Orientation.NORTH;
        makeDecisionPicture();
    }

    /**
     * OnClick picture South
     * @param v
     */
    public void onClickSouth(android.view.View v){
        lastPictureOrientation = Orientation.SOUTH;
        makeDecisionPicture();
    }

    /**
     * OnClick picture East
     * @param v
     */
    public void onClickEast(android.view.View v){
        lastPictureOrientation = Orientation.EAST;
        makeDecisionPicture();
    }

    /**
     * OnClick picture West
     * @param v
     */
    public void onClickWest(android.view.View v){
        lastPictureOrientation = Orientation.WEST;
        makeDecisionPicture();
    }

    /**
     * AlertBox determining if we need to take a photo, enter in selection mode or cancel
     */
    private void makeDecisionPicture(){
        if (room.getPhoto(lastPictureOrientation) == null){
            takePicture();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
            builder.setTitle(getResources().getString(R.string.doAction));

            builder.setPositiveButton(getResources().getString(R.string.modify), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(RoomActivity.this, SelectActivity.class);
                    intent.putExtra("ObjectRecyclerParentName", habitation.getName());
                    intent.putExtra("ObjectRecyclerName", room.getName());
                    intent.putExtra("Orientation", lastPictureOrientation.toString());
                    startActivity(intent);
                }
            });
            builder.setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.retakePicture), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    takePicture();
                }
            });

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Initialize launcher for picture result when taken
     */
    private void launcherInit(){
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            saveImage();
                        }
                    }
                }
        );
    }

    /**
     * Take a picture, launches MediaStore.ACTION_IMAGE_CAPTURE
     */
    private void takePicture() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            File pictureFile = null;
            try { pictureFile = newImageFile();} catch (IOException ex) { ex.printStackTrace(); }
            if (pictureFile != null) {
                Uri pictureURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", pictureFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        pictureURI);
                currPicturePath = pictureURI;
                launcher.launch(pictureIntent);
            }
        }
    }

    /**
     * Create a new image file on disk to save the result
     * @return
     * @throws IOException
     */
    private File newImageFile() throws IOException {
        IdMaker idMaker = IdMaker.getInstance();
        String uniqueName = "image" + idMaker.getUIdImg();
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                uniqueName,
                ".jpg",
                storageDir
        );

        return image;
    }

    /**
     * Save image to the room object, delete the previous one if existing
     */
    private void saveImage(){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Photo currPhoto = room.getPhoto(lastPictureOrientation);
        if (currPhoto != null)
        {
            Uri uri = Uri.parse(currPhoto.getImage());
            getContentResolver().delete(uri, null, null);
        }

        Uri cloned = Uri.parse(currPicturePath.toString());
        room.setPhoto(new Photo(lastPictureOrientation, cloned));
        Bitmap imageBitmap = room.getPhoto(lastPictureOrientation).getImageBitmap(this);

        switch (lastPictureOrientation){
            case EAST:
                imgEast.setImageBitmap(imageBitmap);
                break;
            case WEST:
                imgWest.setImageBitmap(imageBitmap);
                break;
            case NORTH:
                imgNorth.setImageBitmap(imageBitmap);
                break;
            case SOUTH:
                imgSouth.setImageBitmap(imageBitmap);
                break;
            default:
                break;
        }
        saveJson(this);
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