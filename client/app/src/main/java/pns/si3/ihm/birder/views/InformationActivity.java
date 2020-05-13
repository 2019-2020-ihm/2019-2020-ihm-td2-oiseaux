package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.reports.MainActivity;

public class InformationActivity extends AppCompatActivity {

    private Button buttonInfoRetour;
    private ImageView imageInfo;
    private TextView textInfoEspece;
    private TextView textInfoName;
    private TextView textInfoNumber;
    private TextView textInfoDate;
    private TextView textInfoAuteur;

    private ReportViewModel reportViewModel;
    private UserViewModel userViewModel;

    private String reportId;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout.
        setContentView(R.layout.activity_information);
        reportId = getIntent().getStringExtra("id");
        initButton();
        initFields();
        initViewModels();

    }



    private void initButton(){
        buttonInfoRetour = (Button) findViewById(R.id.buttonInfoRetour);
        buttonInfoRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFields(){
        imageInfo = (ImageView) findViewById(R.id.imageInfo);
        textInfoEspece = (TextView) findViewById(R.id.textInfoEspece);
        textInfoNumber = (TextView) findViewById(R.id.textInfoNombre);
        textInfoDate = (TextView) findViewById(R.id.textInfoDate);
        textInfoAuteur = (TextView) findViewById(R.id.textInfoAuteur);
        textInfoName = (TextView) findViewById(R.id.textInfoNom);
    }

    /**
     * Initializes the view models that hold the data.
     */
    private void initViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        reportViewModel
        .getReportsLiveData()
                .observe(
                        this,
                        reports -> {
                            for (Report report : reports) {
                                if (report.getId().equals(reportId)) {
                                    this.report = report;
                                }
                            }
                            textInfoEspece.setText("Espèce : " + report.getSpecies());
                            textInfoNumber.setText("Nombre : " + report.getNumber());
                            SimpleDateFormat formatter =new SimpleDateFormat("HH:mm, dd-MM-yyyy ");
                            String date = formatter.format(report.getDate());
                            textInfoDate.setText("Date : " + date);
                            userViewModel.getUser(report.getUserId());
                            userViewModel
                                    .getSelectedUserLiveData()
                                    .observe(
                                            this,
                                            user -> {
                                                if (user != null) {
                                                    textInfoAuteur.setText("Par : " + user.getFirstName() + " " + user.getLastName());
                                                }
                                            }
                                    );
                        }
                );
    }


}
