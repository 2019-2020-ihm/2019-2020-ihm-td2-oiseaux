package pns.si3.ihm.birder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import etudes.fr.demoosm.R;

public class SignalActivity extends AppCompatActivity {

    Button buttonReturn;
    Button buttonModifierImage;
    Button buttonSupprimerImage;
    ImageView imageOiseau;
    EditText editTextHeure;
    EditText editTextDate;
    Boolean imageUpload = false;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        buttonModifierImage = (Button) findViewById(R.id.buttonModifierImage);
        buttonSupprimerImage = (Button) findViewById(R.id.buttonSupprimerImage);
        imageOiseau = (ImageView) findViewById(R.id.imageOiseau);
        editTextHeure = (EditText) findViewById(R.id.editHeure);
        editTextDate = (EditText) findViewById(R.id.editDate);
        buttonReturn = (Button) findViewById(R.id.buttonSignalReturn);

        setVisibilityButtonSupprimerImage();
        setTimeAndDateInEditText();
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        buttonSupprimerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOiseau.setImageResource(R.drawable.gallery);
                setImageUpload(false);
                setVisibilityButtonSupprimerImage();
            }
        });
        buttonModifierImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void setImageUpload(boolean value){
        this.imageUpload = value;
    }

    void setVisibilityButtonSupprimerImage(){
        if(!imageUpload) buttonSupprimerImage.setVisibility(View.INVISIBLE);
        else buttonSupprimerImage.setVisibility(View.VISIBLE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageOiseau.setImageURI(imageUri);
            setImageUpload(true);
            setVisibilityButtonSupprimerImage();
        }
    }

    void setTimeAndDateInEditText(){
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        editTextHeure.setText(currentTime);
        String currentDate = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(new Date());
        editTextDate.setText(currentDate);

    }
}
