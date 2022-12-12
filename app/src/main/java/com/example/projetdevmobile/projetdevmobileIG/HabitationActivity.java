package com.example.projetdevmobile.projetdevmobileIG;

import static com.example.projetdevmobile.tools.Static.saveJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.util.ArrayList;

public class HabitationActivity extends AppCompatActivity {

    private HabitationManager manager;
    private RecyclerView recyclerRoom;
    private Habitation habitation;
    private EditText habNameText;
    private Button defineEntrance;

    private int selectedRoom;
    ArrayList<String> roomsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitation);

        habNameText = (EditText) findViewById(R.id.habitationName);
        recyclerRoom = (RecyclerView) findViewById(R.id.recyclerRoom);
        defineEntrance = (Button)findViewById(R.id.buttonDefineEntrance);
        defineEntrance.setEnabled(false);
        manager = HabitationManager.getInstance();

        roomsName = new ArrayList<>();
        selectedRoom = 0;

        Intent myIntent = getIntent();
        Boolean isCreation = myIntent.getBooleanExtra("isCreation", false);

        if(!isCreation)
            unFocusAndDisplay(myIntent);
        else
            focusAndCreate();

        // Display keyboard when focused
        habNameText.setShowSoftInputOnFocus(true);

        // Handle entry in EditText
        onKeyListenerHabName();
        displayRooms();
    }

    public void onNewRoom(android.view.View v){
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("isCreation", true);
        intent.putExtra("ObjectRecyclerParentName", habitation.getName());
        startActivity(intent);
    }

    public void disableButtonEntrance(){
        if(habitation.getRooms().size() == 0)
             defineEntrance.setEnabled(false);
    }

    public void defineEntrance(android.view.View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitationActivity.this);
        builder.setTitle(getResources().getString(R.string.entry_room));
        roomsName.clear();
        for (ObjectRecycler r : habitation.getRooms()) {
            roomsName.add(r.getName());
        }

        int checkedItem = 0;

        if(habitation.getRoomEntrance() != null) {
            checkedItem = roomsName.indexOf(habitation.getRoomEntrance().getName().toString());
        }
        builder.setSingleChoiceItems(roomsName.toArray(new String[roomsName.size()]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedRoom = which;
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                habitation.setRoomEntrance((Room)habitation.getRoom(roomsName.get(selectedRoom)));
                displayRooms();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayRooms();
        saveJson(this);
    }

    private void unFocusAndDisplay(Intent myIntent){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Hide keyboard
        habNameText.clearFocus();

        String nameOfHab = myIntent.getStringExtra("ObjectRecyclerName");

        this.habitation = manager.getHabitation(nameOfHab);

        if (habitation != null) {
            habNameText.setText(habitation.getName());
            displayRooms();
        }
    }

    private void focusAndCreate(){
        if(habNameText.requestFocus()) { // request focus on EditText, display keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        habitation = new Habitation(getResources().getString(R.string.habitation));
        if(!manager.availiableName(habitation, getResources().getString(R.string.habitation)))
        {
            int i=1;
            while(!manager.availiableName(habitation, getResources().getString(R.string.habitation) +" " + i)){
                i++;
             }
            habitation.setName(getResources().getString(R.string.habitation) + " " + i);
        }

        habNameText.setText(habitation.getName());
        manager.addHabitation(habitation);
    }

    private void onKeyListenerHabName(){
        habNameText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String habName = habNameText.getText().toString();

                    if(habName.contentEquals("") || !manager.availiableName(habitation, habName))
                    {
                        Toast.makeText(HabitationActivity.this, getResources().getString(R.string.entry_room), Toast.LENGTH_SHORT).show();
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

    private void displayRooms(){
        recyclerRoom.setAdapter(new ObjectRecyclerAdapter(HabitationActivity.this, habitation.getRooms()));
        recyclerRoom.setLayoutManager(new LinearLayoutManager(HabitationActivity.this));
        if(habitation.getRooms().size() > 0)
            defineEntrance.setEnabled(true);
        else
            defineEntrance.setEnabled(false);
    }

    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

}