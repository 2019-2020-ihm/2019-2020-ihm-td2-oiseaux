package pns.si3.ihm.birder.views;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import etudes.fr.demoosm.R;

/**
 * Able to load and save a picture (bitmap)
 * save button : able to save a picture given by the parent activity
 * load button : call back onPictureLoad() of the parent activity
 * these buttons can be enable or disable
 */
public class StorageFragment extends Fragment {
    private IStorageActivity activity;

    private Button buttonSave;
    private Button buttonLoad;

    private String pictureName;
    private String directoryName;

    public StorageFragment() {}

    public StorageFragment(IStorageActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);

        pictureName = "test.jpg";

        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        directoryName = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE).getPath(); // /data/user/0/pns.si3.ihm.birder/app_imageDir

        buttonLoad = rootView.findViewById(R.id.button_load);
        buttonSave = rootView.findViewById(R.id.button_save);
        setDisableButtonSave();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap picture = activity.getPictureToSave();
                if (picture != null) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, IStorageActivity.REQUEST_MEDIA_WRITE);
                    } else {
                        saveToInternalStorage(picture);
                        setDisableButtonSave();
                    }
                }
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, IStorageActivity.REQUEST_MEDIA_WRITE);
                } else {
                    activity.onPictureLoad(loadImageFromInternalStorage());
                }
            }
        });

        return rootView;
    }

    public  void saveToInternalStorage(Bitmap picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pictureName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directoryName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        File file = new File(directoryName, pictureName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            picture.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadImageFromInternalStorage() {
        try {
            File file = new File(directoryName, pictureName);

            Toast toast = Toast.makeText(getContext(), "Picture load", Toast.LENGTH_LONG);
            toast.show();

            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDisableButtonSave() {
        buttonSave.setEnabled(false);
    }

    public void setEnableButtonSave() {
        buttonSave.setEnabled(true);
    }

    public void setDisableButtonLoad() {
        buttonLoad.setEnabled(false);
    }

    public void setEnableButtonLoad() {
        buttonLoad.setEnabled(true);
    }

    public Button getButtonSave() {
        return buttonSave;
    }

    public void setButtonSave(Button buttonSave) {
        this.buttonSave = buttonSave;
    }

    public Button getButtonLoad() {
        return buttonLoad;
    }

    public void setButtonLoad(Button buttonLoad) {
        this.buttonLoad = buttonLoad;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
