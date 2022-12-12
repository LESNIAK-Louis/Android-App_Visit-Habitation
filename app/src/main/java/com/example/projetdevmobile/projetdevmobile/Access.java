package com.example.projetdevmobile.projetdevmobile;

import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

public class Access {
    private Room room;
    private Rect rect;

    public Access(Room room, Rect rect){
        this.room = room;
        this.rect = rect;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Rect getRect(){return rect;}

    public Room getRoom(){return room;}


    public boolean equals(Access a){
        return this.rect.toString().contentEquals(a.rect.toString());
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("room", this.room.getName());
        jsonObject.put("rectL", this.getRect().left);
        jsonObject.put("rectR", this.getRect().right);
        jsonObject.put("rectT", this.getRect().top);
        jsonObject.put("rectB", this.getRect().bottom);

        return jsonObject;
    }
}
