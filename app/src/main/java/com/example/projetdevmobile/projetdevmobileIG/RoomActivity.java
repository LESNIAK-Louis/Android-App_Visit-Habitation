package com.example.projetdevmobile.projetdevmobileIG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Habitation;

public class RoomActivity extends AppCompatActivity {


    private ActivityResultLauncher<Intent> launcher;
    private Habitation habitation;
    private EditText roomNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        laucherInit();

        Intent myIntent = getIntent();
        Boolean isCreation = myIntent.getBooleanExtra("isCreation", true);

        /*
        intent.putExtra("ObjectRecyclerName", resultDialogText);
        intent.putExtra("ObjectRecyclerParentName", habitation.getName());
        */

        if(!isCreation) {

        }
        else{
            Toast.makeText(RoomActivity.this, "Creating", Toast.LENGTH_SHORT).show();
        }
    }

    private void laucherInit(){
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
    }
}