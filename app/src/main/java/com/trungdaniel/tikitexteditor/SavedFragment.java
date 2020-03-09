package com.trungdaniel.tikitexteditor;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trungdaniel.tikitexteditor.view.adapter.SavedAdapter;
import com.trungdaniel.tikitexteditor.view.model.Spacecraft;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {
    private RecyclerView rvSaved;
    private ImageView imgBackSaved;
    private NavController navController;
    private Button btnMorePhoto;
    private TextView tvNull;

    public SavedFragment() {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


    }

    private ArrayList<Spacecraft> getData() {
        ArrayList<Spacecraft> spacecrafts = new ArrayList<>();
        //TARGET FOLDER
        File downloadsFolder = Environment.getExternalStoragePublicDirectory("TextOnPhoto");

        Spacecraft s;

        if (downloadsFolder.exists()) {
            //GET ALL FILES IN DOWNLOAD FOLDER
            File[] files = downloadsFolder.listFiles();

            //LOOP THRU THOSE FILES GETTING NAME AND URI
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                s = new Spacecraft();
                s.setUri(Uri.fromFile(file));

                spacecrafts.add(s);
            }
        }


        return spacecrafts;
    }


    private void init(View view) {
        tvNull = view.findViewById(R.id.tv_saved);
        rvSaved = view.findViewById(R.id.rv_saved);
        rvSaved.setLayoutManager(new GridLayoutManager(getContext(), 2));
        SavedAdapter savedAdapter = new SavedAdapter(getData(), getContext());
        rvSaved.setAdapter(savedAdapter);
        imgBackSaved = view.findViewById(R.id.img_back_saved);
        navController = Navigation.findNavController(view);
        btnMorePhoto = view.findViewById(R.id.btn_edit_photo);
        btnMorePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_savedFragment_to_libraryFragment);
            }
        });
        imgBackSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_savedFragment_to_homeFragment);
            }
        });

        // check saved is null
        if (savedAdapter.getItemCount() == 0) {
            tvNull.setText("You don't have any workart here");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

}
