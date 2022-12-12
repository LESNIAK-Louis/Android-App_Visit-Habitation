package com.example.projetdevmobile.projetdevmobile;

import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class determining a Habitation
 */
public class Habitation implements ObjectRecycler {
    private String name;
    private ArrayList<ObjectRecycler> rooms;
    private Room roomEntrance;

    /**
     * Public constructor
     * @param name name of the habitation
     */
    public Habitation(String name){
        rooms = new ArrayList<ObjectRecycler>();
        this.name = name;
        this.roomEntrance = null;
    }

    /**
     * Getter of the name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Getter of the type of the object
     * @return ObjectType
     */
    @Override
    public ObjectType getType() {
        return ObjectType.HABITATION;
    }

    /**
     * Getter of the entrance
     * @return null or the room in question
     */
    public Room getRoomEntrance() {
        return roomEntrance;
    }

    /**
     * Getter of the number of rooms in the habitation
     * @return number of rooms
     */
    public int getNbRooms(){
        return getRooms().size();
    }

    /**
     * Getter of all rooms
     * @return rooms
     */
    public ArrayList<ObjectRecycler> getRooms(){ return rooms; }

    public ObjectRecycler getRoom(String name){
        for(ObjectRecycler room : rooms){
            if(room.getName().contentEquals(name))
                return room;
        }
        return null;
    }

    /**
     * Setter of romm entrance
     * @param roomEntrance
     */
    public void setRoomEntrance(Room roomEntrance) {
        this.roomEntrance = roomEntrance;
    }

    /**
     * Setter of the name of the habitation
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        for(ObjectRecycler r : rooms){
            ((Room)r).setHabitationName(this.name);
        }
    }

    /**
     * Create and add a room to the habitation
     * @param name of the room to create
     * @return room created
     */
    public Room addRoom(String name){
        Room room = new Room(name, this.name);
        this.rooms.add(room);
        return room;
    }

    /**
     * Add an existing room to the habitation
     * @param room
     */
    public void addRoom(Room room){
        rooms.add(room);
    }

    /**
     * Remove a room of the habitation
     * @param room
     */
    public void removeRoom(Room room){
        this.rooms.remove(room);
    }

    /**
     * Check if the Habitation is correct for visit
     * @return boolean
     */
    public boolean isCorrect(){
        if(rooms.size() == 0)
            return false;
        for(ObjectRecycler r : rooms){
            if(!( ((Room)r).isCorrect() ))
                return false;
        }
        return true;
    }

    /**
     * Check if the name passed in parameter is availiable for the room passed in parameter
     * @param room check name for this room
     * @param name name in question
     * @return boolean
     */
    public boolean availiableRoomName(Room room, String name){
        for(ObjectRecycler uRoom : rooms){
            if(uRoom.getName().contentEquals(name) && !room.equals((Room)uRoom))
                return false;
        }
        return true;
    }

    /**
     * Convert habitation to json for save
     * @return JSONObject
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("nbRooms", rooms.size());

        JSONArray jsonArrayRoom = new JSONArray();
        for(ObjectRecycler room : rooms){
            jsonArrayRoom.put(((Room)room).toJson());
        }

        jsonObject.put("rooms", jsonArrayRoom);

        if(this.roomEntrance != null)
            jsonObject.put("roomEntrance", this.roomEntrance.getName());
        else
            jsonObject.put("roomEntrance", "null");

        return jsonObject;
    }
}
