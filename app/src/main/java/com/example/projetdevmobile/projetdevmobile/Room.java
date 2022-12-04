package com.example.projetdevmobile.projetdevmobile;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.IdMaker;

import java.util.HashMap;

public class Room implements ObjectRecycler {
    private HashMap<Orientation, Photo> photos;
    private Habitation habitation;
    private String name;

    public Room(String name, Habitation habitation){
        this.name = name;
        this.habitation = habitation;
        photos = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.ROOM;
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

    public void setName(String name){
        this.name = name;
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
}
