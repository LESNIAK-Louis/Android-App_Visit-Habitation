package com.example.projetdevmobile.projetdevmobile;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.IdMaker;

import java.util.HashMap;

public class Room implements ObjectRecycler {
    private HashMap<Orientation, Photo> photos;
    private Habitation habitation;
    private String name;
    private boolean start;

    public Room(String name, Habitation habitation){
        this.name = name;
        this.habitation = habitation;
        photos = new HashMap<>();
        start = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.ROOM;
    }

    public boolean isStart() {
        return start;
    }

    public Photo getPhoto(Orientation o) {
        if(photos != null && o != null) {
            return photos.get(o);
        }
        return null;
    }

    public String getHabitationName(){
        return habitation.getName();
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhoto(Photo photo){
        if(photo != null)
            photos.put(photo.getOrientation(), photo);
    }

    public boolean isCorrect(){
        if(getPhoto(Orientation.NORTH) == null || getPhoto(Orientation.SOUTH) == null
                || getPhoto(Orientation.EAST) == null || getPhoto(Orientation.WEST) == null)
            return false;
        return true;
    }

    public boolean isSet(Orientation o){
        for(Orientation orientationPhoto : this.photos.keySet()){
            if(orientationPhoto.equals(o))
                return true;
        }
        return false;
    }
}
