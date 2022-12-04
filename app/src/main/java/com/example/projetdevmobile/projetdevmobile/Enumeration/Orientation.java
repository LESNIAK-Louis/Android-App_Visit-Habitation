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

    public String toString()
    {
        return this.nom;
    }
}