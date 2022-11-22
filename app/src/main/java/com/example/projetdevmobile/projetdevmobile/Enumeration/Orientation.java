package com.example.projetdevmobile.projetdevmobile.Enumeration;

public enum Orientation{

    NORTH("Bleu"),
    SOUTH("Rouge"),
    EAST("Vert"),
    WEST("Jaune");

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