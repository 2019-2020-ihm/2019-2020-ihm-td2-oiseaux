package pns.si3.ihm.birder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import etudes.fr.demoosm.R;

public class SignalActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        button = (Button) findViewById(R.id.buttonSignalReturn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
