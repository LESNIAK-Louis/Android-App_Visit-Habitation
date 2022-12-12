package com.example.projetdevmobile.projetdevmobileIG;


import static com.example.projetdevmobile.tools.Static.saveJson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ObjectRecyclerAdapter extends RecyclerView.Adapter<ObjectRecyclerAdapter.ObjectViewHolder> {
    LayoutInflater inflater;
    List<ObjectRecycler> modelList;

    public ObjectRecyclerAdapter(Context context, List<ObjectRecycler> modelList) {
        this.inflater = LayoutInflater.from(context);
        this.modelList = modelList;
    }

    @Override
    public ObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectViewHolder holder, int position) {
        ObjectRecycler object = modelList.get(position);

        /* Associate correct icons depending of the status of the object */

        holder.image4.setImageResource(R.drawable.trash);

        if(object.getType().equals(ObjectType.ROOM)) {
            Room room = ((Room) object);

            holder.image1.setImageResource(R.drawable.room);
            if(!room.isCorrect()) {
                holder.image2.setImageResource(R.drawable.warning);
            }


            HabitationManager hm = HabitationManager.getInstance();
            Habitation hab = hm.getHabitation(((Room)object).getHabitationName());
            Room r = hab.getRoomEntrance();
            if(r != null && r.getName().contentEquals(room.getName())) {
                holder.image3.setImageResource(R.drawable.start);
            }

            holder.image4.setOnClickListener(l->{
                alertDeletion(object);
            });
        }
        else{
            Habitation hab = ((Habitation) object);

            holder.image1.setImageResource(R.drawable.hab);
            if(!hab.isCorrect() || hab.getRoomEntrance() == null){
                holder.image2.setImageResource(R.drawable.warning);
            }
            else{
                holder.image2.setImageResource(R.drawable.play);
                holder.image2.setOnClickListener(l->{
                    Intent intent = new Intent(inflater.getContext(), VisitActivity.class);
                    intent.putExtra("HabitationName", object.getName());
                    inflater.getContext().startActivity(intent);
                });
            }

            holder.image4.setOnClickListener(l->{
                alertDeletion(object);
            });
        }


        // Good dimensions
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75, 75);
        holder.image1.setLayoutParams(layoutParams);
        holder.image2.setLayoutParams(layoutParams);
        holder.image3.setLayoutParams(layoutParams);
        holder.image4.setLayoutParams(layoutParams);

        holder.nameObject.setText(object.getName());

        /* Listener on click item */

        holder.itemView.setOnClickListener(l->{
            Intent intent = new Intent(inflater.getContext(), object.getType().equals(ObjectType.HABITATION)?HabitationActivity.class:RoomActivity.class); // Get real type of ObjectRecycler to start the correct Activity
            intent.putExtra("isCreation",false);
            intent.putExtra("ObjectRecyclerName", object.getName());

            if(object.getType().equals(ObjectType.ROOM))
                intent.putExtra("ObjectRecyclerParentName", ((Room)object).getHabitationName());

            inflater.getContext().startActivity(intent);
        });
    }

    /**
     * Display an alert for confirmation when attemping to delete  an ObjectRecycler
     * @param object
     */
    private void alertDeletion(ObjectRecycler object){
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle(inflater.getContext().getResources().getString(R.string.delete) + object.getName() + " ?");

        builder.setPositiveButton(inflater.getContext().getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(object.getType().equals(ObjectType.ROOM)){
                    deleteRoom(object);
                    HabitationActivity habAct = ((HabitationActivity)(inflater.getContext()));
                    habAct.disableButtonEntrance();
                    notifyDataSetChanged();
              }else{
                  HabitationManager hm = HabitationManager.getInstance();
                  for(ObjectRecycler room : hm.getHabitation(object.getName()).getRooms())
                  {
                      deleteRoom(room);
                  }
                  hm.removeHabitation(((Habitation)object));
                  saveJson(inflater.getContext());

                  notifyDataSetChanged();
              }
            }
        });
        builder.setNeutralButton(inflater.getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Delete a room properly
     * @param object
     */
    private void deleteRoom(ObjectRecycler object){
        HabitationManager hm = HabitationManager.getInstance();
        Habitation hab = hm.getHabitation(((Room)object).getHabitationName());

        Room r = hab.getRoomEntrance();
        if(r != null && hab.getRoomEntrance().getName().contentEquals(((Room)object).getName()))
            hab.setRoomEntrance(null);

        // Deletion of all accesses and pictures assiociated
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                for(ObjectRecycler object_hab : hab.getRooms()){
                    Room room = (Room)object_hab;
                    for(Photo photo : room.getPhotos().values()){
                        ArrayList<Access> accessesToDel = new ArrayList<>();
                        for(Access a : photo.getAccess())
                        {
                            if(a.getRoom().getName().contentEquals(((Room)object).getName()))
                                accessesToDel.add(a);
                        }
                        for(Access aToDel : accessesToDel)
                        {
                            photo.removeAccess(aToDel);
                        }
                    }
                }

                Room room = (Room)object;
                for(Photo photo : room.getPhotos().values()){
                    Uri uri = Uri.parse(photo.getImage());
                    inflater.getContext().getContentResolver().delete(uri, null, null);
                }
                hab.removeRoom(room);
                saveJson(inflater.getContext());
            }
        });
    }

    /**
     * Get number of items in RecyclerView
     * @return
     */
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    /**
     * Item layout
     */
    class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameObject;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;

        public ObjectViewHolder(View itemView) {
            super(itemView);
            nameObject = (TextView) itemView.findViewById(R.id.nameObjectRecycler);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
        }
    }
}