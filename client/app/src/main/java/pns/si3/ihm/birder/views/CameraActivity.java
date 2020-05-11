package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import etudes.fr.demoosm.R;

public class CameraActivity extends AppCompatActivity implements IPictureActivity, IStorageActivity {

    Button buttonReturn;
    private Bitmap picture;
    private PictureFragment pictureFragment;
    private StorageFragment storageFragment;
    Button buttonValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        buttonReturn = findViewById(R.id.buttonCameraReturn);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        pictureFragment = (PictureFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPicture);
        if (pictureFragment == null) {
            pictureFragment = new PictureFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentPicture, pictureFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        storageFragment = (StorageFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStorage);
        if (storageFragment == null) {
            storageFragment = new StorageFragment(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentStorage, storageFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        buttonValider = findViewById(R.id.buttonValiderImage);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signal = new Intent(CameraActivity.this, ReportActivity.class);
                startActivityForResult(signal, IPictureActivity.PICK_IMAGE);
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    // Callback from requestPermission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), "CAMERA authorisation granted", Toast.LENGTH_LONG);
                    toast.show();

                    pictureFragment.takePicture();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "CAMERA authorisation not granted", Toast.LENGTH_LONG);
                    toast.show();
                }
            } break;
            case REQUEST_MEDIA_WRITE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Write authorisation granted", Toast.LENGTH_LONG);
                    toast.show();

                    storageFragment.saveToInternalStorage(picture);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Write authorisation not granted", Toast.LENGTH_LONG);
                    toast.show();
                }
            } break;
            case REQUEST_MEDIA_READ: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Read authorisation granted", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Read authorisation not granted", Toast.LENGTH_LONG);
                    toast.show();
                }
            } break;
        }
    }

    // Callback from startActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                picture = (Bitmap) data.getExtras().get("data");
                pictureFragment.setImageView(picture);
                storageFragment.setEnableButtonSave();
            } else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "PICTURE canceled", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "PICTURE action failed", Toast.LENGTH_LONG);
                toast.show();
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                Uri pictureUri = data.getData();
                pictureFragment.setImageURI(pictureUri);
        }
    }

    @Override
    public void onPictureLoad(Bitmap bitmap) {
        pictureFragment.setImageView(bitmap);
    }

    @Override
    public Bitmap getPictureToSave() {
        return picture;
    }
}
