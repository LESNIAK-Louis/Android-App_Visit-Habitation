package com.example.projetdevmobile.projetdevmobile;

import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;

import java.util.ArrayList;

public class Habitation implements ObjectRecycler {
    private String name;
    private ArrayList<ObjectRecycler> rooms;
    private Room roomEntrance;

    public Habitation(String name){
        rooms = new ArrayList<ObjectRecycler>();
        this.name = name;
        this.roomEntrance = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.HABITATION;
    }

    public Room getRoomEntrance() {
        return roomEntrance;
    }

    public int getNbRooms(){
        return getRooms().size();
    }

    public ArrayList<ObjectRecycler> getRooms(){ return rooms; }

    public ObjectRecycler getRoom(String name){
        for(ObjectRecycler room : rooms){
            if(room.getName().contentEquals(name))
                return room;
        }
        return null;
    }

    public void setRoomEntrance(Room roomEntrance) {
        this.roomEntrance = roomEntrance;
    }

    public void setName(String name) {
        this.name = name;
        for(ObjectRecycler r : rooms){
            ((Room)r).setHabitationName(this.name);
        }
    }

    public Room addRoom(String name){
        Room room = new Room(name, this.name);
        this.rooms.add(room);
        return room;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public void removeRoom(Room room){
        this.rooms.remove(room);
    }

    public boolean isCorrect(){
        if(rooms.size() == 0)
            return false;
        for(ObjectRecycler r : rooms){
            if(!( ((Room)r).isCorrect() ))
                return false;
        }
        return true;
    }

    public boolean availiableRoomName(Room room, String name){
        for(ObjectRecycler uRoom : rooms){
            if(uRoom.getName().contentEquals(name) && !room.equals((Room)uRoom))
                return false;
        }
        return true;
    }
}
