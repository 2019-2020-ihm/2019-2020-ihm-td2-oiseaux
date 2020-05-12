package pns.si3.ihm.birder.views.notifications;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.reports.MainActivity;
import pns.si3.ihm.birder.views.MapActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;
import static pns.si3.ihm.birder.views.notifications.NotificationApp.CHANNEL_ID;

public class NotificationActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private ListView listView;
    private CheckBox checkBox;
    private ImageView imageView;
     ArrayList<NotificationItem> notifications;
    private FirebaseAuth auth;
    private Boolean allNotification = true;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;
    private String userId;
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
        initViewModel();
        initListOfNotification();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNotification(!getAllNotification());
                changeBooleanAllNotification();
                if(checkBox.isChecked()){
                    sendNotificationChannel(CHANNEL_ID,NotificationCompat.PRIORITY_DEFAULT, "merle_noir");
                }
            }});


        imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //TODO io
                 //Implémenter l'ajout d'une notification
                 //Récupérer l'oiseau en affichant une liste avec les oiseaux ?
                 //Mettre à jour la liste des oiseaux notifiés
             }});

    }

    void init(){
        auth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.listview_notification);
        imageView = (ImageView) findViewById(R.id.imageview_add_notification);
        checkBox = (CheckBox) findViewById(R.id.checkbox_all_notif);
        setSpinner();
    }

    private void initViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userId = authViewModel.getAuthenticationId();
        userViewModel.getUser(userId);
        userViewModel
                .getUserLiveData()
                .observe(
                        this,
                        user -> {
                            if (user != null) {
                                setAllNotification(user.getAllNotificationActivate());
                            }
                            checkBox.setChecked(user.getAllNotificationActivate());
                        }
                );
    }


    void initListOfNotification(){
        notifications = new ArrayList<NotificationItem>();
        notifications.add(new NotificationItem("Merle noir", "0",R.drawable.merle_noir));
        NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications,  NotificationActivity.this);
        listView.setAdapter(notificationAdapter);
    }

    void changeBooleanAllNotification(){
        userViewModel.getUser(userId);
        userViewModel
                .getUserLiveData()
                .observe(
                        this,
                        user -> {
                            if (user != null) {
                                userViewModel
                                        .getUserLiveData()
                                        .observe(
                                                this,
                                                databaseUser -> {
                                                    if (databaseUser != null) {
                                                        user.setAllNotificationActivate(getAllNotification());
                                                        userViewModel.setUser(user);
                                                    }
                                                }
                                        );
                                }
                        }
                );
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:break;
            case 1: // Liste signalisation
            {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            break;
            case 2: // Map
            {
                Intent intent = new Intent(NotificationActivity.this, MapActivity.class);
                startActivity(intent);
            }break;
            case 3: //Compte (connecté) / Se connecter (déconnecté)
            {
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(NotificationActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(NotificationActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
            case 4:// Déconnexion (connecté)
            {
                // The user is connected.
                if (auth.getCurrentUser() != null) {
                    // Sign out the user.
                    auth.signOut();

                    // Success toast.
                    Toast.makeText(
                            NotificationActivity.this,
                            "Vous avez été déconnecté !",
                            Toast.LENGTH_SHORT
                    ).show();
                    Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                // The user is not connected.
                else {
                    // Navigate to sign in.
                    Intent intent = new Intent(NotificationActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinner(){
        final Spinner spinner = findViewById(R.id.spinner_notification);
        spinner.setAdapter(null);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<>();
        list.add("Menu");
        list.add("Dernières signalisations");
        list.add("Voir Carte");
        // The user is connected.
        if (auth.getCurrentUser() != null) {
            list.add("Compte");
            list.add("Se déconnecter");
        }
        // The user is not connected.
        else {
            list.add("Se connecter");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void setAllNotification(Boolean value){
        this.allNotification = value;
    }

    private Boolean getAllNotification(){
        return allNotification;
    }

    public Boolean notificationActivate(String birdId){
        boolean notifActivate = getAllNotification();
        if(!notifActivate){
            userViewModel.getUser(userId);
            userViewModel
                    .getUserLiveData()
                    .observe(
                            this,
                            user -> {
                                if (user != null) {
                                    // TODO io
                                    // Parcourir le tableau de string avec les id des oiseaux notifiés
                                    }
                                }
                    );
        }
        return getAllNotification();
    }

    private String getNameOfBirdFromId(String birdId){
        String nameBird = null;
        // TODO io
        // chercher dans la base de données l'oiseau associé à l'id
        return nameBird;
    }


    public void sendNotificationChannel(String channelId, int priority, String nameBird){
        int drawableId = getResources().getIdentifier(nameBird, "drawable", getPackageName());
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.bird)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), drawableId))
                .setContentTitle("Nouveau signalement")
                .setContentText("L'oiseau " + nameBird +" vient d\'être signalé !")
                .setTimeoutAfter(3600000)
                .setPriority(priority);
        NotificationApp.getNotificationManager().notify(++notificationId, notification.build());

    }


}
