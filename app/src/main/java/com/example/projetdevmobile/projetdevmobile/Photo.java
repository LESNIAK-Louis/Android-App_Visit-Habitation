package com.example.projetdevmobile.projetdevmobile;

import static com.example.projetdevmobile.tools.Static.handleSamplingAndRotationBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

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
    private Uri imgName;

    public Photo(){
        access = new ArrayList<>();
    }

    public Photo(Orientation orientation, Uri imgName){
        this.orientation = orientation;
        this.imgName = imgName;
        access = new ArrayList<>();
    }

    public Photo(Orientation orientation, Coords coord, Uri imgName){
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
        return imgName.toString();
    }

    public ArrayList<Access> getAccess(){return access;}

    public boolean checkIntersect(Rect rect){
        for(Access a : access){
            if(rect.intersect(a.getRect())){
                return  true;
            }
        }
        return false;
    }

    public Bitmap getImageBitmap(Context context) {
        Bitmap imageBitmap = null;
        try {
            imageBitmap = handleSamplingAndRotationBitmap(context, imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }


    public void setImage(Uri image) {
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
