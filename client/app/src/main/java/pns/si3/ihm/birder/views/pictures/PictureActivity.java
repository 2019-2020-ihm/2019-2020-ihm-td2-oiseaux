package pns.si3.ihm.birder.views.pictures;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import etudes.fr.demoosm.R;

/**
 * Picture activity.
 *
 * Allows the user to take a picture, save a picture or load a picture.
 */
public class PictureActivity extends AppCompatActivity {
	public static final int REQUEST_TAKE_PICTURE = 1;
	public static final int REQUEST_LOAD_PICTURE = 2;
	public static final int REQUEST_SAVE_PICTURE = 3;

	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "PictureActivity";

	private ImageView image;

	/**
	 * The selected picture.
	 */
	private Uri pictureUri;

	/**
	 * The activity buttons.
	 */
	private Button returnButton;
	private Button takePictureButton;
	private Button loadPictureButton;
	private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initButtons();
		image = findViewById(R.id.image);

		ContextWrapper contextWrapper = new ContextWrapper(this);
		pictureUri = null;
    }

	/**
	 * Initializes the activity buttons.
	 */
	private void initButtons() {
		// Return button.
		returnButton = findViewById(R.id.button_return);
		returnButton.setOnClickListener(v -> {
			finish();
		});

		// Take picture button.
		takePictureButton = findViewById(R.id.button_take_picture);
		takePictureButton.setOnClickListener(v -> {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PICTURE);
			} else {
				takePicture();
			}
		});

		// Load picture.
		loadPictureButton = findViewById(R.id.button_load_picture);
		loadPictureButton.setOnClickListener(v -> {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOAD_PICTURE);
			} else {
				loadPicture();
			}
		});

		// Submit button.
		submitButton = findViewById(R.id.button_submit);
		submitButton.setOnClickListener(v -> {
			if (pictureUri != null) {
				Intent intent = new Intent();
				intent.putExtra("pictureURI", pictureUri);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	/**
	 * Method triggered when the user allows or denies a permission request.
	 * @param requestCode The request code.
	 * @param permissions The permission requests.
	 * @param grantResults The permissions results.
	 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
        	// Take picture request.
            case REQUEST_TAKE_PICTURE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Granted.
                   takePicture();
                } else {
                	// Not granted.
                   	Toast.makeText(
                   		this,
						"L'utilisation de la caméra n'est pas autorisée.",
						Toast.LENGTH_SHORT
					).show();
                }
				break;
            }
        	// Load picture request.
            case REQUEST_LOAD_PICTURE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Granted.
					loadPicture();
				} else {
					// Not granted.
					Toast.makeText(
						this,
						"La lecture de la mémoire n'est pas autorisée.",
						Toast.LENGTH_SHORT
					).show();
                }
				break;
            }
        }
    }

	/**
	 * Method triggered when the activity receives a result.
	 * @param requestCode The request code.
	 * @param resultCode The result code.
	 * @param data The result data.
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			// Camera request.
			case REQUEST_TAKE_PICTURE: {
				switch (resultCode) {
					// Picture received.
					case RESULT_OK: {
						image.setImageURI(pictureUri);
						break;
					}
					// Picture canceled.
					case RESULT_CANCELED: {
						Toast.makeText(
							this,
							"Aucune photo n'a été prise.",
							Toast.LENGTH_SHORT
						).show();
						break;
					}
					// Picture failed.
					default: {
						Toast.makeText(
							this,
							"Une erreur est survenue.",
							Toast.LENGTH_SHORT
						).show();
						break;
					}
				}
				break;
			}
			// Pick image request.
			case REQUEST_LOAD_PICTURE: {
				switch (resultCode) {
					// Picture received.
					case RESULT_OK: {
						pictureUri = data.getData();
						image.setImageURI(pictureUri);
						break;
					}
					// Picture canceled.
					case RESULT_CANCELED: {
						Toast.makeText(
							this,
							"Aucune photo n'a été sélectionnée.",
							Toast.LENGTH_SHORT
						).show();
						break;
					}
					// Picture failed.
					default: {
						Toast.makeText(
							this,
							"Une erreur est survenue.",
							Toast.LENGTH_SHORT
						).show();
						break;
					}
				}
				break;
			}
		}
    }


	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
			imageFileName,
			".jpg",
			storageDir
		);
		return image;
	}

	/**
	 * Makes the user take a picture.
	 */
	private void takePicture() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				pictureUri = FileProvider.getUriForFile(
					this,
					"pns.si3.ihm.birder.fileprovider",
					photoFile
				);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
			}
		}
	}

	/**
	 * Make the user load a picture from the gallery.
	 */
	public void loadPicture() {
		Intent loadPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		if (loadPictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(loadPictureIntent, REQUEST_LOAD_PICTURE);
		}
	}
}
