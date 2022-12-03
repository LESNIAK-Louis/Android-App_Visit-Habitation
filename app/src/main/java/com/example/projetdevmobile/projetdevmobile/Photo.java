package com.example.projetdevmobile.projetdevmobile;

import android.media.Image;

import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.Coords;

import java.util.ArrayList;

public class Photo {
    private Orientation orientation;
    private ArrayList<Access> access;
    private Coords coords;
    private Image image;

    public Photo(){
        access = new ArrayList<>();
    }

    public Orientation getOrientation(){
        return this.orientation;
    }

    public Coords getCoords() {
        return coords;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setOrientation(Orientation o){
        this.orientation = o;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public void addAccess(Access a){
        this.access.add(a);
    }

    public void removeAccess(Access a){
        this.access.remove(a);
    }

    public boolean equals(Photo photo){
        return this.orientation.equals(photo.getOrientation()) && this.coords.equals(photo.coords) && this.image.equals(photo.image);
    }
}
