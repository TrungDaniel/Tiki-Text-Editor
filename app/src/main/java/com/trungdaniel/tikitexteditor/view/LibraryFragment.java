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


    class ImageFolder {
        private String path;
        private String FolderName;
        private int numberOfPics = 0;
        private String firstPic;

        public ImageFolder() {
        }

        public ImageFolder(String path, String folderName) {
            this.path = path;
            FolderName = folderName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFolderName() {
            return FolderName;
        }

        public void setFolderName(String folderName) {
            FolderName = folderName;
        }

        public int getNumberOfPics() {
            return numberOfPics;
        }

        public void setNumberOfPics(int numberOfPics) {
            this.numberOfPics = numberOfPics;
        }

        public void addpics() {
            this.numberOfPics++;
        }

        public String getFirstPic() {
            return firstPic;
        }

        public void setFirstPic(String firstPic) {
            this.firstPic = firstPic;
        }
    }



    private ArrayList<String> getPicturePaths() {
        ArrayList<String> listForder= new ArrayList<>();
        ArrayList<ImageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = getContext().getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                ImageFolder folds = new ImageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);
                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }
        for (int i=0;i<picFolders.size();i++){
            listForder.add(picFolders.get(i).getFolderName());
        }
        return listForder;




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

        //  Log.d("Files", "Size: " + files.length);
        arr.add("All file");
        for (int i = 0; i < files.length; i++) {
            arr.add(getPicturePaths().get(i));
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
                       /* rvPhoto.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        rvPhoto.setAdapter(new SavedAdapter(getData(spinner.getSelectedItem().toString()), getContext()));*/
                        rvPhoto.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        rvPhoto.setAdapter(new PhotoAdapter(getAllImagesByFolder(spinner.getSelectedItem().toString()), getContext()));

                    } catch (NullPointerException e) {

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public ArrayList<String> getAllImagesByFolder(String path) {
        ArrayList<String> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = getContext().getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                String pic = new String();
                pic = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<String> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
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
