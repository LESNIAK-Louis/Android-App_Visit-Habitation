package com.example.projetdevmobile.projetdevmobile.Enumeration;

public enum Orientation{

    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West");

    private String nom;

    private Orientation(String nom)
    {
        this.nom = nom;
    }

    public String getOrientation()
    {
        return this.nom;
    }

    public static Orientation getRight(Orientation o){
        switch (o){
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            case EAST:
                return SOUTH;
        }
        return NORTH;
    }

    public static  Orientation getLeft(Orientation o){
        switch (o){
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            case EAST:
                return NORTH;
        }
        return NORTH;
    }

    public static Orientation getOrientation(String o){
        Orientation orientation;
        if(o.contentEquals("North"))
            orientation = Orientation.NORTH;
        else if (o.contentEquals("South"))
            orientation = Orientation.SOUTH;
        else if (o.contentEquals("West"))
            orientation = Orientation.WEST;
        else
            orientation = Orientation.EAST;

        return orientation;
    }

    public String toString()
    {
        return this.nom;
    }
}