package com.example.projetdevmobile.projetdevmobileIG;

import static com.example.projetdevmobile.tools.Static.openJson;
import static com.example.projetdevmobile.tools.Static.saveJson;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.projetdevmobile.R;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(openJson(this))
            startActivity(new Intent(this, HabitationManagerActivity.class));
    }

    public void goHome(android.view.View v){
        startActivity(new Intent(this, HabitationManagerActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveJson(this);
    }


}