package com.example.projetdevmobile.tools;

public class IdMaker {
    private int id;

    /**
     * Private constructor
     */
    public IdMaker(){
        this.id = 0;
    }

    /**
     * Return an unique id starting from 1
     * @return id
     */
    public int getId(){

        return id++;
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