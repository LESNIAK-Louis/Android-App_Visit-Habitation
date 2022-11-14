package com.example.projetdevmobile.tools;

public class Coords {
    private float x;
    private float y;
    private float z;

    public Coords(float x, float y, float z){
        this.setCoords(x,y,z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setCoords(float x, float y, float z){
        this.x = x;
        this.z = z;
        this.y = y;
    }

    public boolean equals(Coords c){
        return this.x == c.getX() && this.y == c.getY() && this.z == c.getZ();
    }
}
