package com.example.projetdevmobile.projetdevmobile;

import java.util.ArrayList;

public class Habitation {
    private ArrayList<Room> rooms;

    public Habitation(){
        rooms = new ArrayList<>();
    }

    public void addRoom(Room r){
        this.rooms.add(r);
    }

    public void delRoom(Room r){
        this.rooms.remove(r);
    }

}
