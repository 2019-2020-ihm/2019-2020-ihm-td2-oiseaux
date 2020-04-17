package pns.si3.ihm.birder;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import etudes.fr.demoosm.R;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        findViewById(R.id.button_return_inscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
