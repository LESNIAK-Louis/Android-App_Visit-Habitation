package com.example.projetdevmobile.projetdevmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.Coords;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Photo {
    private Orientation orientation;
    private ArrayList<Access> access;
    private Coords coords;
    private String imgName;

    public Photo(){
        access = new ArrayList<>();
    }

    public Photo(Orientation orientation, String imgName){
        this.orientation = orientation;
        this.imgName = imgName;
        access = new ArrayList<>();
    }

    public Photo(Orientation orientation, Coords coord, String imgName){
        this.orientation = orientation;
        this.imgName = imgName;
        this.coords = coord;
        access = new ArrayList<>();
    }

    public Orientation getOrientation(){
        return this.orientation;
    }

    public Coords getCoords() {
        return coords;
    }

    public String getImage() {
        return imgName;
    }

    public Bitmap getImageBitmap(File parent) {
        Bitmap bm = null;
            try {
                InputStream fis = new FileInputStream(new File(parent,this.imgName));
                if(fis != null) {
                    bm = BitmapFactory.decodeStream(fis);
                    fis.close();
                }
            } catch (FileNotFoundException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
        return bm;
    }


    public void setImage(String image) {
        this.imgName = image;
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
        return this.orientation.equals(photo.getOrientation()) && this.coords.equals(photo.coords) && this.getImage().equals(photo.getImage());
    }
}
