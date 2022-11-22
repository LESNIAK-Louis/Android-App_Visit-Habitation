package com.example.projetdevmobile.projetdevmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HabitationManager {
    private static HabitationManager instance = new HabitationManager();

    ArrayList<ObjectRecycler> habitations;

    private HabitationManager(){ this.habitations = new ArrayList<>(); }

    public static HabitationManager getInstance() {
        return instance;
    }

    public ArrayList<ObjectRecycler> getHabitations(){ return habitations; }

    public Habitation getHabitation(String name){
        for(ObjectRecycler hab : habitations){
            if(hab.getName().contentEquals(name))
                return (Habitation)hab;
        }
        return null;
    }

    public boolean availiableName(Habitation habitation, String name){
        for(ObjectRecycler hab : habitations){
            if(hab.getName().contentEquals(name) && !habitation.equals((Habitation)hab))
                return false;
        }
        return true;
    }

    public void addHabitation(Habitation hab){ this.habitations.add(hab); }
    public void removeHabitation(Habitation hab){ this.habitations.remove(hab); }

}
