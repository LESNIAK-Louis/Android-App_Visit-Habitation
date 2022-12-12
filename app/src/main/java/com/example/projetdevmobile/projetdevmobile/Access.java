package com.example.projetdevmobile.projetdevmobile;

import android.graphics.Rect;

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
}
