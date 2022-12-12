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

/**
 * Class that represent a picture
 */
public class Photo {
    private Orientation orientation;
    private ArrayList<Access> access;
    private Uri imgName;

    /**
     * Public constructor
     */
    public Photo(){
        access = new ArrayList<>();
    }

    /**
     * Public constructor
     * @param orientation
     * @param imgName
     */
    public Photo(Orientation orientation, Uri imgName){
        this.orientation = orientation;
        this.imgName = imgName;
        access = new ArrayList<>();
    }

    /**
     * Getter of the orientation
     * @return
     */
    public Orientation getOrientation(){
        return this.orientation;
    }

    /**
     * Getter of the Uri to String (path of image saved on disk)
     * @return imgName.toString()
     */
    public String getImage() {
        return imgName.toString();
    }

    /**
     * Getter of all accesses
     * @return access
     */
    public ArrayList<Access> getAccess(){return access;}

    /**
     * Check if the rect passed in parameter intersects with an other rect in all accesses
     * @param rect
     * @return boolean
     */
    public boolean checkIntersect(Rect rect){
        for(Access a : access){
            Rect rectA = a.getRect();
            if(rect.intersects(rectA.left, rectA.top, rectA.right, rectA.bottom)){
                return  true;
            }
        }
        return false;
    }

    /**
     * Getter of the image Bitmap
     * @param context
     * @return image in Bitmap
     */
    public Bitmap getImageBitmap(Context context) {
        Bitmap imageBitmap = null;
        try {
            imageBitmap = handleSamplingAndRotationBitmap(context, imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }


    /**
     * Setter of the Uri image
     * @param image
     */
    public void setImage(Uri image) {
        this.imgName = image;
    }

    /**
     * Setter of the orientation
     * @param o
     */
    public void setOrientation(Orientation o){
        this.orientation = o;
    }

    /**
     * Add an access to the photo
     * @param a
     */
    public void addAccess(Access a){
        this.access.add(a);
    }

    /**
     * Remove an access to the photo
     * @param a
     */
    public void removeAccess(Access a){
        this.access.remove(a);
    }

    /**
     * Check if the photo passed in parameters is equals to the current photo
     * @param photo
     * @return boolean
     */
    public boolean equals(Photo photo){
        return this.orientation.equals(photo.getOrientation()) && this.getImage().equals(photo.getImage());
    }

    /**
     * Convert the photo manager to json for save
     * @return JSONObject
     * @throws JSONException
     */
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
