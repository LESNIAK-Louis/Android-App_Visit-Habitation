package com.example.projetdevmobile.projetdevmobileIG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.util.List;

public class ObjectRecyclerAdapter extends RecyclerView.Adapter<ObjectRecyclerAdapter.ObjectViewHolder> {
    LayoutInflater inflater;
    List<ObjectRecycler> modelList;

    public ObjectRecyclerAdapter(Context context, List<ObjectRecycler> modelList) {
        this.inflater = LayoutInflater.from(context);
        this.modelList = modelList;
    }

    @Override
    public ObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler, parent, false);
        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectViewHolder holder, int position) {
        ObjectRecycler object = modelList.get(position);
        holder.nameObject.setText(object.getName());
        holder.itemView.setOnClickListener(l->{
            Intent intent = new Intent(inflater.getContext(), object.getType().equals(ObjectType.HABITATION)?HabitationActivity.class:RoomActivity.class); // Get real type of ObjectRecycler to start the correct Activity
            intent.putExtra("isCreation",false);

            if(object.getType().equals(ObjectType.ROOM))
                intent.putExtra("ObjectRecyclerParentName", ((Room)object).getHabitationName());
            else
                intent.putExtra("ObjectRecyclerName", object.getName());

            inflater.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameObject;

        public ObjectViewHolder(View itemView) {
            super(itemView);
            nameObject = (TextView) itemView.findViewById(R.id.nameObjectRecycler);
        }

    }
}