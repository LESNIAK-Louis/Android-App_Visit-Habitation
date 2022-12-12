package com.example.projetdevmobile.projetdevmobile.Enumeration;

/**
 * Enum of dertermining the orientation of a picture
 */
public enum Orientation{

    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West");

    private String orientation;

    /**
     * Private constructor
     * @param orientation
     */
    private Orientation(String orientation)
    {
        this.orientation = orientation;
    }

    /**
     * Getter of orientation
     * @return nom
     */
    public String getOrientation()
    {
        return this.orientation;
    }

    /**
     * Return actual orientation + 90 degrees
     * @param o actual orientation
     * @return
     */
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

    /**
     * Return actual orientation - 90 degrees
     * @param o actual orientation
     * @return
     */
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

    /**
     * Getter of an Orientation object from a string
     * @param o orientation in string
     * @return orientation converted
     */
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

    /**
     * toString
     * @return string
     */
    public String toString()
    {
        return this.orientation;
    }
}