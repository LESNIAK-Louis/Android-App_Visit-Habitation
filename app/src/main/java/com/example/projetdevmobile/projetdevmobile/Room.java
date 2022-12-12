package com.example.projetdevmobile.projetdevmobile;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.tools.IdMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class that represents a Room
 */
public class Room implements ObjectRecycler {
    private HashMap<Orientation, Photo> photos;
    private String  habitationName;
    private String name;

    /**
     * Public constructor
     * @param name
     * @param habitationName
     */
    public Room(String name, String habitationName){
        this.name = name;
        this.habitationName = habitationName;
        photos = new HashMap<>();
    }

    /**
     * Getter of the room's name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Getter of the ObjectType
     * @return ROOM
     */
    @Override
    public ObjectType getType() {
        return ObjectType.ROOM;
    }

    /**
     * Getter of the photo corresponding to the orientation passed in parameters
     * @param o
     * @return null or the photo in question
     */
    public Photo getPhoto(Orientation o) {
        if(photos != null && o != null) {
            return photos.get(o);
        }
        return null;
    }

    /**
     * Getter of the habitation name that the room belongs to
     * @return
     */
    public String getHabitationName(){
        return habitationName;
    }

    /**
     * Getter of all photos
     * @return HashMap<Orientation, Photo>
     */
    public HashMap<Orientation, Photo> getPhotos() {
        return photos;
    }

    /**
     * Setter of the name of the room
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Setter of habitation's name
     * @param habitationName
     */
    public void setHabitationName(String habitationName) {
        this.habitationName = habitationName;
    }

    /**
     * Setter of a photo
     * @param photo
     */
    public void setPhoto(Photo photo){
        if(photo != null)
            photos.put(photo.getOrientation(), photo);
    }

    /**
     * Check if each orientation is associated to a photo
     * @return boolean
     */
    public boolean isCorrect(){
        if(getPhoto(Orientation.NORTH) == null || getPhoto(Orientation.SOUTH) == null
                || getPhoto(Orientation.EAST) == null || getPhoto(Orientation.WEST) == null)
            return false;
        return true;
    }

    /**
     * Check if a photo is set for a specific orientation
     * @param o
     * @return
     */
    public boolean isSet(Orientation o){
        for(Orientation orientationPhoto : this.photos.keySet()){
            if(orientationPhoto.equals(o))
                return true;
        }
        return false;
    }

    /**
     * Convert the room to json for save
     * @return JSONObject
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());

        jsonObject.put("nbPhotos", photos.values().size());

        JSONArray jsonArrayPhoto = new JSONArray();
        for(Photo photo : photos.values()){
            jsonArrayPhoto.put(photo.toJson());
        }
        jsonObject.put("photos", jsonArrayPhoto);

        return jsonObject;
    }
}
