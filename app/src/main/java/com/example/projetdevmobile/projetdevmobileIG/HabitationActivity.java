package com.example.projetdevmobile.projetdevmobileIG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.Room;

public class HabitationActivity extends AppCompatActivity {

    private HabitationManager manager;
    private RecyclerView recyclerRoom;
    private Habitation habitation;
    private EditText habNameText;

    private String resultDialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitation);

        habNameText = (EditText) findViewById(R.id.habitationName);
        recyclerRoom = (RecyclerView) findViewById(R.id.recyclerRoom);
        manager = HabitationManager.getInstance();

        resultDialogText = new String();

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
        habNameText.setText(habitation.getName()); // Unconfirmed name, put the original name back

        Intent intent = new Intent(this, RoomActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a room name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultDialogText = input.getText().toString();
                if(resultDialogText.contentEquals(""))
                    onNewRoom(v);
                else {
                    intent.putExtra("isCreation", true);
                    intent.putExtra("ObjectRecyclerName", resultDialogText);
                    intent.putExtra("ObjectRecyclerParentName", habitation.getName());
                    startActivity(intent);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultDialogText = "";
                input.clearFocus();
                habNameText.clearFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Hide keyboard
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayRooms();
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

        habitation = new Habitation("Habitation");
        if(!manager.availiableName(habitation, "Habitation"))
        {
            int i=1;
            while(!manager.availiableName(habitation, "Habitation " + i)){
                i++;
             }
            habitation.setName("Habitation " + i);
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

    private void displayRooms(){
        recyclerRoom.setAdapter(new ObjectRecyclerAdapter(HabitationActivity.this, habitation.getRoomManager().getRooms()));
        recyclerRoom.setLayoutManager(new LinearLayoutManager(HabitationActivity.this));
    }

    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

}