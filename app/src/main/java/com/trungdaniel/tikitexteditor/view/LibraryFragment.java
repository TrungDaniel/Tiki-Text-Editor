package com.trungdaniel.tikitexteditor.view;


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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.trungdaniel.tikitexteditor.R;
import com.trungdaniel.tikitexteditor.view.adapter.PhotoAdapter;
import com.trungdaniel.tikitexteditor.view.adapter.SavedAdapter;
import com.trungdaniel.tikitexteditor.view.model.Spacecraft;

import java.io.File;
import java.util.ArrayList;


public class LibraryFragment extends Fragment {
    private RecyclerView rvPhoto;
    private PhotoAdapter photoAdapter;
    private Spinner spinner;
    private ImageView imgBackLibraly;
    private NavController navController;


    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getAllFile(view);


    }

    private void getAllImage() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getContext().getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }

        rvPhoto.setLayoutManager(new GridLayoutManager(getContext(), 3));
        photoAdapter = new PhotoAdapter(listOfAllImages, getContext());
        rvPhoto.setAdapter(photoAdapter);
    }

    private void getAllFile(View view) {
        spinner = view.findViewById(R.id.sp_file);
        ArrayList<String> arr = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        arr.add("All file");
        for (int i = 0; i < files.length; i++) {
            arr.add(files[i].getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        arr
                );
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        getAllImage();


                }

                if (spinner.getSelectedItem().toString() != "All file") {
                    try {
                        rvPhoto.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        rvPhoto.setAdapter(new SavedAdapter(getData(spinner.getSelectedItem().toString()), getContext()));
                    } catch (NullPointerException e) {

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private ArrayList<Spacecraft> getData(String name) {

        ArrayList<Spacecraft> spacecrafts = new ArrayList<>();
        //TARGET FOLDER
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(name);

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
        navController = Navigation.findNavController(view);
        rvPhoto = view.findViewById(R.id.rv_photo);
        imgBackLibraly = view.findViewById(R.id.img_back_library);
        imgBackLibraly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_libraryFragment_to_homeFragment);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }


}
