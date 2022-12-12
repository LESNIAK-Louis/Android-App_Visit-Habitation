package com.example.projetdevmobile.projetdevmobile.Enumeration;

/**
 * Enum of dertermining the type of an object
 */
public enum ObjectType {

    HABITATION("Habitation"),
    ROOM("Room");

    private String typeOfObject;

    /**
     * Private constructor of ObjectType
     * @param typeOfObject
     */
    private ObjectType(String typeOfObject)
    {
        this.typeOfObject = typeOfObject;
    }

    public String toString()
    {
        return this.typeOfObject;
    }
}