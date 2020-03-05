package com.trungdaniel.tikitexteditor.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trungdaniel.tikitexteditor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    private Button btnDone;
    private ImageView imgHome;
    private ImageView imgResultCamera;
    private NavController navController;
    private Bundle bundle;

    public EditFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        navController = Navigation.findNavController(view);
        bundle = getArguments();
        final String uriData = bundle.getString("uri");
        imgResultCamera = view.findViewById(R.id.img_result_camera);
        Glide.with(view).load(uriData).into(imgResultCamera);

        btnDone = view.findViewById(R.id.btn_done);
        imgHome = view.findViewById(R.id.img_home);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_editFragment2_to_homeFragment);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("uri", uriData);

                navController.navigate(R.id.action_editFragment2_to_saveFragment, bundle);
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

}
