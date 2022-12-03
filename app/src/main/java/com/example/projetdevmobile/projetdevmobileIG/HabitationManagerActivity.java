package com.example.projetdevmobile.projetdevmobileIG;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;

import java.util.ArrayList;

public class HabitationManagerActivity extends AppCompatActivity {

    private HabitationManager manager;
    private RecyclerView recyclerHab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitation_manager);
        manager = HabitationManager.getInstance();
        recyclerHab = (RecyclerView)findViewById(R.id.recyclerHab);

        manager.addHabitation(new Habitation("test"));


        displayHabitations();
    }

    public void onNewHab(android.view.View v){
        Intent intent = new Intent(this, HabitationActivity.class);
        intent.putExtra("isCreation",true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayHabitations();
    }

    private void displayHabitations(){
        recyclerHab.setAdapter(new ObjectRecyclerAdapter(HabitationManagerActivity.this, manager.getHabitations()));
        recyclerHab.setLayoutManager(new LinearLayoutManager(HabitationManagerActivity.this));
    }
}