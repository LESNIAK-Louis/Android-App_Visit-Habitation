package com.example.projetdevmobile.projetdevmobile;
import com.example.projetdevmobile.tools.IdMaker;

import java.util.HashMap;

public class Room {
    private HashMap<Orientation, Photo> photos;
    private int id;

    public Room(){
        id = IdMaker.getInstance().getId();
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
