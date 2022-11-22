package com.example.projetdevmobile.projetdevmobile;

import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;

import java.util.ArrayList;

public class Habitation implements ObjectRecycler {
    private String name;
    private ArrayList<ObjectRecycler> rooms;

    public Habitation(String name){
        rooms = new ArrayList<>();
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.HABITATION;
    }

    public ArrayList<ObjectRecycler> getRooms(){
        return rooms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoom(Room r){
        this.rooms.add(r);
    }

    public void removeRoom(Room r){
        this.rooms.remove(r);
    }
}
