package com.example.projetdevmobile.projetdevmobileIG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;

public class RoomActivity extends AppCompatActivity {

    private Habitation habitation;
    private EditText roomNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent myIntent = getIntent();
        Boolean isCreation = myIntent.getBooleanExtra("isCreation", true);

        if(!isCreation) {}
        else{
            Toast.makeText(RoomActivity.this, "Creating", Toast.LENGTH_SHORT).show();
        }
    }
}