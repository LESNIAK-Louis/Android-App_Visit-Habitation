package com.example.projetdevmobile.tools;

public class IdMaker {
    private int id;
    private static IdMaker instance = new IdMaker();

    /**
     * Getter of class instance
     * @return instance
     */
    public static IdMaker getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private IdMaker(){
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
     * Reset the counter
     */
    public void reset(){
        this.id = 0;
    }
}