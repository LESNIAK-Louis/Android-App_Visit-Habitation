package com.example.projetdevmobile.projetdevmobile;

import static com.example.projetdevmobile.tools.Static.handleSamplingAndRotationBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;

import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Photo {
    private Orientation orientation;
    private ArrayList<Access> access;
    private Uri imgName;

    public Photo(){
        access = new ArrayList<>();
    }

    public Photo(Orientation orientation, Uri imgName){
        this.orientation = orientation;
        this.imgName = imgName;
        access = new ArrayList<>();
    }


    public Orientation getOrientation(){
        return this.orientation;
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

    public void addAccess(Access a){
        this.access.add(a);
    }

    public void removeAccess(Access a){
        this.access.remove(a);
    }

    public boolean equals(Photo photo){
        return this.orientation.equals(photo.getOrientation()) && this.getImage().equals(photo.getImage());
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orientation", this.getOrientation().toString());

        if(imgName != null)
            jsonObject.put("imgName", imgName.toString());
        else
            jsonObject.put("imgName", "null");

        jsonObject.put("nbAccess", access.size());

        JSONArray jsonArrayAccess = new JSONArray();
        for(Access access : access){
            jsonArrayAccess.put(access.toJson());
        }
        jsonObject.put("access", jsonArrayAccess);

        return jsonObject;
    }

}
