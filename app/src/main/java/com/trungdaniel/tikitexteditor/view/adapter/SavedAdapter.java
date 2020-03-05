package com.trungdaniel.tikitexteditor.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.trungdaniel.tikitexteditor.R;
import com.trungdaniel.tikitexteditor.view.model.Spacecraft;
import java.util.ArrayList;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {
    private ArrayList<Spacecraft> photos;
    private Context context;

    public SavedAdapter(ArrayList<Spacecraft> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_saved, parent, false);

        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(photos.get(position).getUri())
                .centerCrop()
                .placeholder(R.drawable.image_1)
                .into(holder.imgPhotoSave);

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhotoSave;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhotoSave = itemView.findViewById(R.id.img_item);
        }
    }
}
