package com.example.projetdevmobile.projetdevmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that manage Habitations (Singleton)
 */
public class HabitationManager {
    private static HabitationManager instance = new HabitationManager();

    ArrayList<ObjectRecycler> habitations;

    /**
     * Private Constructor
     */
    private HabitationManager(){ this.habitations = new ArrayList<>(); }

    /**
     * Static getter of the instance
     * @return instance
     */
    public static HabitationManager getInstance() {
        return instance;
    }

    /**
     * Getter of all habitations
     * @return habitations
     */
    public ArrayList<ObjectRecycler> getHabitations(){ return habitations; }

    /**
     * Getter of a specific habitation by name
     * @param name name of the habitation to search
     * @return null or the object in question
     */
    public Habitation getHabitation(String name){
        for(ObjectRecycler hab : habitations){
            if(hab.getName().contentEquals(name))
                return (Habitation)hab;
        }
        return null;
    }

    /**
     * Check if the availiable name passed in parameter is availiable for the habitation passed in parameter
     * @param habitation to check for name
     * @param name name in question
     * @return
     */
    public boolean availiableName(Habitation habitation, String name){
        for(ObjectRecycler hab : habitations){
            if(hab.getName().contentEquals(name) && !habitation.equals((Habitation)hab))
                return false;
        }
        return true;
    }

    /**
     * Add a habitation to the manager
     * @param hab
     */
    public void addHabitation(Habitation hab){ this.habitations.add(hab); }

    /**
     * Remove the habitation passed in parameter from the manager
     * @param hab
     */
    public void removeHabitation(Habitation hab){ this.habitations.remove(hab); }

    /**
     * Convert the habitation manager to json for save
     * @return JSONObject
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArrayHab = new JSONArray();
            jsonObject.put("nbHabitations", habitations.size());

            for (ObjectRecycler hab : habitations) {
                Habitation habToSave = (Habitation) hab;
                jsonArrayHab.put(habToSave.toJson());
            }
            jsonObject.put("habitations", jsonArrayHab);
        return jsonObject;
    }

}

