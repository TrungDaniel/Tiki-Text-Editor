package com.trungdaniel.tikitexteditor.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trungdaniel.tikitexteditor.R;
import com.trungdaniel.tikitexteditor.view.model.Photo;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private ArrayList<String> photos;
    private Context context;
    private NavController navController;
    private Bundle bundle;

    public PhotoAdapter(ArrayList<String> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        navController = Navigation.findNavController(parent);
        bundle = new Bundle();
        return new PhotoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder holder, final int position) {

        Glide
                .with(context)
                .load(photos.get(position))
                .centerCrop()
                .placeholder(R.drawable.image_1)
                .into(holder.imgPhoto);
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("uri", photos.get(position));
                navController.navigate(R.id.action_libraryFragment_to_editFragment2, bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item);
        }
    }
}
