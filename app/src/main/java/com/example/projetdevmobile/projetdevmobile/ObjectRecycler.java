package com.example.projetdevmobile.projetdevmobile;

import android.os.Parcelable;

import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;

/**
 * Interface of an Object fitting in a RecyclerView
 */
public interface ObjectRecycler {
    public abstract String getName();
    public abstract ObjectType getType();
}
