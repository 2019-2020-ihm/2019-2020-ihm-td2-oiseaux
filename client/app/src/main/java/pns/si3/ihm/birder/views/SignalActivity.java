package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    Uri imageUri;
    TextView buttonPositionActuelle;
    TextView buttonChoisirCarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        buttonModifierImage    = (Button)    findViewById(R.id.buttonModifierImage);
        buttonSupprimerImage   = (Button)    findViewById(R.id.buttonSupprimerImage);
        imageOiseau            = (ImageView) findViewById(R.id.imageOiseau);
        editTextHeure          = (EditText)  findViewById(R.id.editHeure);
        editTextDate           = (EditText)  findViewById(R.id.editDate);
        buttonReturn           = (Button)    findViewById(R.id.buttonSignalReturn);
        buttonPositionActuelle = (TextView)  findViewById(R.id.textPositionActuelle);
        buttonChoisirCarte     = (TextView)  findViewById(R.id.textChoisirCarte);

        setTimeAndDateInEditText();

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        setVisibilityButtonSupprimerImage();
        buttonSupprimerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOiseau.setImageResource(R.drawable.gallery);
                setImageUpload(false);
                setVisibilityButtonSupprimerImage();
            }
        });

        setTextButtonModifierImage();
        buttonModifierImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        buttonPositionActuelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonChoisirCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carte = new Intent(SignalActivity.this, GPSActivity.class);
                startActivity(carte);
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

    void setTextButtonModifierImage() {
        if (imageUpload) buttonModifierImage.setText("Modifier l'image");
        else buttonModifierImage.setText("Ajouter une image");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IPictureActivity.PICK_IMAGE){
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
