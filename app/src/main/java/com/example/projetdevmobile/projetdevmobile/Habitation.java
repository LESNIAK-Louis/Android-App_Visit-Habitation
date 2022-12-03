package com.example.projetdevmobile.projetdevmobile;

import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;

import java.util.ArrayList;

public class Habitation implements ObjectRecycler {
    private String name;
    private RoomManager roomManager;

    public Habitation(String name){
        roomManager = new RoomManager();
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

    public RoomManager getRoomManager(){
        return roomManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoom(String name){
        roomManager.addRoom(name, this);
    }

    public void removeRoom(Room r){
        roomManager.removeRoom(r);
    }
}
