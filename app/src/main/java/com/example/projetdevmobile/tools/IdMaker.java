package com.example.projetdevmobile.tools;

import java.util.Calendar;

public class IdMaker {
    private int id;
    private int idImg;
    private static IdMaker instance = new IdMaker();

    /**
     * Private constructor
     */
    private IdMaker(){
        this.id = 0;
        this.idImg = 0;
    }

    public static IdMaker getInstance() {
        return instance;
    }

    /**
     * Return an unique id starting from 1
     * @return id
     */
    public int getId(){

        return id++;
    }

    /**
     * Return an unique id for image storage
     * @return uid
     */
    public String getUIdImg(){

        return Calendar.getInstance().getTimeInMillis() + "" + id++;
    }

    /**
     * Remove 1 from the counter
     */
    public void decId(){
        id--;
    }

    /**
     * Reset the counter
     */
    public void reset(){
        this.id = 0;
    }
}