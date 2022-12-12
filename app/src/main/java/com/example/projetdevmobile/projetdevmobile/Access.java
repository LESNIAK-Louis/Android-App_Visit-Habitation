package com.example.projetdevmobile.projetdevmobile;

import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class determining an access between two rooms
 */
public class Access {
    private Room room;
    private Rect rect;

    /**
     * Public constructor
     * @param room destination
     * @param rect rectangle to display on the picture
     */
    public Access(Room room, Rect rect){
        this.room = room;
        this.rect = rect;
    }

    /**
     * Stter room
     * @param room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Getter rect
     * @return rect
     */
    public Rect getRect(){return rect;}

    /**
     * Getter room
     * @return room
     */
    public Room getRoom(){return room;}

    /**
     * Check if two access are equals
     * @param a access to compare
     * @return boolean
     */
    public boolean equals(Access a){
        return this.rect.toString().contentEquals(a.rect.toString());
    }

    /**
     * Convert access to json for save
     * @return JSONObject
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if(room != null)
            jsonObject.put("room", this.room.getName());
        else
            jsonObject.put("room", "null");

        jsonObject.put("rectL", this.getRect().left);
        jsonObject.put("rectR", this.getRect().right);
        jsonObject.put("rectT", this.getRect().top);
        jsonObject.put("rectB", this.getRect().bottom);

        return jsonObject;
    }
}
