package com.example.projetdevmobile.projetdevmobileIG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;

public class HabitationActivity extends AppCompatActivity {

    private HabitationManager manager;
    private RecyclerView recyclerRoom;
    private Habitation habitation;
    private EditText habNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitation);

        habNameText = (EditText) findViewById(R.id.habitationName);
        recyclerRoom = (RecyclerView) findViewById(R.id.recyclerRoom);

        Intent myIntent = getIntent();
        Boolean isCreation = myIntent.getBooleanExtra("isCreation", true);

        if(!isCreation) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Hide keyboard

            String nameOfHab = myIntent.getStringExtra("ObjectRecyclerName");

            manager = HabitationManager.getInstance();
            this.habitation = manager.getHabitation(nameOfHab);

            if (habitation != null) {
                habNameText.setText(habitation.getName());
                displayRooms();
            }
        }
        else{
            if(habNameText.requestFocus()) { // request focus on EditText, display keyboard
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

        // Display keyboard when focused
        habNameText.setShowSoftInputOnFocus(true);

        // Handle entry in EditText
        habNameText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String habName = habNameText.getText().toString();

                    if(habName.contentEquals("") || !manager.availiableName(habitation, habName))
                    {
                        Toast.makeText(HabitationActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        habNameText.setText(habitation.getName());
                    }
                    else{
                        habitation.setName(habName);
                        hideKeyboard(v);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayRooms();
    }

    private void displayRooms(){
        recyclerRoom.setAdapter(new ObjectRecyclerAdapter(HabitationActivity.this, habitation.getRooms()));
        recyclerRoom.setLayoutManager(new LinearLayoutManager(HabitationActivity.this));
    }

    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}