package com.example.projetdevmobile.projetdevmobileIG;

import static com.example.projetdevmobile.tools.Static.saveJson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;

import java.util.concurrent.Executors;

public class HabitationManagerActivity extends AppCompatActivity {

    private HabitationManager manager;
    private RecyclerView recyclerHab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitation_manager);
        manager = HabitationManager.getInstance();
        recyclerHab = (RecyclerView)findViewById(R.id.recyclerHab);

        displayHabitations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.itemQuitApp: System.exit(0); break;
            case R.id.itemSave:
                Context ctx = this;
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    public void run() {
                        saveJson(ctx);
                    }
                });
                break;
            case R.id.itemFinish: this.finish(); break;
            default: super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Button new habitation
     * @param v
     */
    public void onNewHab(android.view.View v){
        Intent intent = new Intent(this, HabitationActivity.class);
        intent.putExtra("isCreation",true);
        startActivity(intent);
    }

    /**
     * When resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        displayHabitations();
        saveJson(this);
    }

    /**
     * Display all habitation in the recycler view
     */
    private void displayHabitations(){
        recyclerHab.setAdapter(new ObjectRecyclerAdapter(HabitationManagerActivity.this, manager.getHabitations()));
        recyclerHab.setLayoutManager(new LinearLayoutManager(HabitationManagerActivity.this));
    }
}