package com.example.projetdevmobile.projetdevmobileIG;


import android.content.Context;
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
import com.example.projetdevmobile.projetdevmobile.Enumeration.ObjectType;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.ObjectRecycler;
import com.example.projetdevmobile.projetdevmobile.Room;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectViewHolder holder, int position) {
        ObjectRecycler object = modelList.get(position);

        if(object.getType().equals(ObjectType.ROOM)) {
            Room room = ((Room) object);

            holder.image1.setImageResource(R.drawable.room);
            if(!room.isCorrect()) {
                holder.image2.setImageResource(R.drawable.warning);
            }
            if(room.isStart()) {
                holder.image3.setImageResource(R.drawable.start);
            }
        }
        else{
            Habitation hab = ((Habitation) object);

            holder.image1.setImageResource(R.drawable.hab);
            if(!hab.isCorrect()){
                holder.image2.setImageResource(R.drawable.warning);
            }
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75, 75);
        holder.image1.setLayoutParams(layoutParams);
        holder.image2.setLayoutParams(layoutParams);
        holder.image3.setLayoutParams(layoutParams);

        holder.nameObject.setText(object.getName());
        holder.itemView.setOnClickListener(l->{
            Intent intent = new Intent(inflater.getContext(), object.getType().equals(ObjectType.HABITATION)?HabitationActivity.class:RoomActivity.class); // Get real type of ObjectRecycler to start the correct Activity
            intent.putExtra("isCreation",false);
            intent.putExtra("ObjectRecyclerName", object.getName());

            if(object.getType().equals(ObjectType.ROOM))
                intent.putExtra("ObjectRecyclerParentName", ((Room)object).getHabitationName());

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

    private Bitmap ScaleImage(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int requiredWidth=75;
        int requiredHeight=75;

        Matrix matrix = new Matrix();
        matrix.postScale(requiredWidth/width, requiredHeight/height);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }


    class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameObject;
        ImageView image1;
        ImageView image2;
        ImageView image3;

        public ObjectViewHolder(View itemView) {
            super(itemView);
            nameObject = (TextView) itemView.findViewById(R.id.nameObjectRecycler);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
        }

    }
}