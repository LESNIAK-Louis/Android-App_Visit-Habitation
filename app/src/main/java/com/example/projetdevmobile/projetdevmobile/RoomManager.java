package com.example.projetdevmobile.projetdevmobile;

import com.example.projetdevmobile.tools.IdMaker;

import java.util.ArrayList;

public class RoomManager {
    ArrayList<ObjectRecycler> rooms;
    IdMaker idMaker;

    public RoomManager(){
        this.rooms = new ArrayList<>();
        this.idMaker = new IdMaker();
    }

    public ArrayList<ObjectRecycler> getRooms(){ return rooms; }

    public ObjectRecycler getRoom(int id){
        for(ObjectRecycler room : rooms){
            if(((Room)room).getId() == id)
                return room;
        }
        return null;
    }

    public void addRoom(String name, Habitation habitation){ this.rooms.add(new Room(name, habitation, idMaker.getId())); }

    public void removeRoom(Room room){
        int id = room.getId();
        if(this.rooms.remove(room)) {
            for (ObjectRecycler otherRoom : rooms) {
                if (((Room)otherRoom).getId() > id)
                    ((Room)otherRoom).setId(room.getId() - 1);
            }
            idMaker.decId();
        }

    }

}
