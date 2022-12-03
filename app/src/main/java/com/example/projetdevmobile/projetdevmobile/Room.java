package com.example.projetdevmobile.projetdevmobile;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.IdMaker;

import java.util.HashMap;

public class Room implements ObjectRecycler {
    private HashMap<Orientation, Photo> photos;
    private Habitation habitation;
    private String name;
    private int id;

    public Room(String name, Habitation habitation, int id){
        this.name = name;
        this.habitation = habitation;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.ROOM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Photo getPhoto(Orientation o) {
        if(photos != null && o != null) {
            photos.get(o);
        }
        return null;
    }

    public String getHabitationName(){
        return habitation.getName();
    }

    public void setPhoto(Photo photo){
        if(photo != null)
            photos.put(photo.getOrientation(), photo);
    }

    public boolean isSet(Orientation o){
        for(Orientation orientationPhoto : this.photos.keySet()){
            if(orientationPhoto.equals(o))
                return true;
        }
        return false;
    }

    public boolean equals(Room r){
        return r.getId() == this.id;
    }
}
