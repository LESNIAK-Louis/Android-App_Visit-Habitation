package com.example.projetdevmobile.projetdevmobile.Enumeration;

public enum ObjectType {

    HABITATION("Habitation"),
    ROOM("Room");

    private String typeOfObject;

    private ObjectType(String typeOfObject)
    {
        this.typeOfObject = typeOfObject;
    }

    public String getObjectType()
    {
        return this.typeOfObject;
    }

    public String toString()
    {
        return this.typeOfObject;
    }
}