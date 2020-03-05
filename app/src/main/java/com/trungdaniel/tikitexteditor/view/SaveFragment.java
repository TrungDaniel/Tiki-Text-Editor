package com.trungdaniel.tikitexteditor.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trungdaniel.tikitexteditor.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveFragment extends Fragment {
    private Toolbar toolbar;
    private ImageView imgSavePhoto, imgBackSave;
    private Button btnSaveImage, btnShareImage;
    private BitmapDrawable bitmapDrawable;
    private Bitmap bitmap;
    private NavController navController;

    public SaveFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadImage(view);
        saveAndShare();
    }

    private void saveAndShare() {
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                getActivity().startActivity(intent);*/
            }
        });

        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapDrawable = (BitmapDrawable) imgSavePhoto.getDrawable();
                bitmap = bitmapDrawable.getBitmap();

                FileOutputStream fileOutputStream = null;

                File fileSave = Environment.getExternalStorageDirectory();
                File directory = new File(fileSave.getAbsolutePath() + "/TextOnPhoto");
                directory.mkdir();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(directory, fileName);


                try {
                    Toast.makeText(getContext(), "bạn đã lưu ảnh thành công", Toast.LENGTH_SHORT).show();
                    fileOutputStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    getActivity().sendBroadcast(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                navController.navigate(R.id.action_saveFragment_to_homeFragment);

            }
        });
    }


    private void loadImage(View view) {
        final Bundle bundle = getArguments();
        Glide.with(view).load(bundle.getString("uri")).into(imgSavePhoto);
    }

    private void init(View view) {
        final Bundle bundle = getArguments();
        bundle.putString("uri", bundle.getString("uri"));
        imgBackSave = view.findViewById(R.id.img_back_save);
        btnSaveImage = view.findViewById(R.id.btn_save_image);
        btnShareImage = view.findViewById(R.id.btn_share);
        toolbar = view.findViewById(R.id.tl_save);
        imgSavePhoto = view.findViewById(R.id.img_image_save);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(null);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navController = Navigation.findNavController(view);
        imgBackSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_saveFragment_to_editFragment2, bundle);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

}
