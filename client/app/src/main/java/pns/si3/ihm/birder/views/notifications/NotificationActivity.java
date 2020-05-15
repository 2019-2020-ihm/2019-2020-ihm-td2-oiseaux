package pns.si3.ihm.birder.views.notifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.ChoiceSpeciesActivity;
import pns.si3.ihm.birder.views.reports.MainActivity;
import pns.si3.ihm.birder.views.reports.MapActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;

public class NotificationActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private ListView listView;
    private CheckBox checkBox;
    private ImageView imageView;
    private FirebaseAuth auth;
    private Boolean allNotification = true;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;
    private SpeciesViewModel speciesViewModel;
    private String userId;
    public static final int REQUEST_SPECIES = 1;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
        initViewModel();

        setListAndCheckBoxInInit();
        checkBox.setOnClickListener(v -> {
            setAllNotification(!getAllNotification());
            changeBooleanAllNotification();
            Log.i("Notif", "Notif bool = " + getAllNotification());
        });
        imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivityForResult(new Intent(NotificationActivity.this, ChoiceSpeciesActivity.class), REQUEST_SPECIES);
             }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SPECIES && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle != null){
                String speciesChoosed = (String) bundle.get("name");
                addItemNotificationForUser(speciesChoosed);
                adapter.notifyDataSetChanged();
            }
        }
    }



    void init(){
        auth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.listview_notification);
        imageView = (ImageView) findViewById(R.id.imageview_add_notification);
        checkBox = (CheckBox) findViewById(R.id.checkbox_all_notif);
        setSpinner();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!listView.getItemAtPosition(position).toString().isEmpty()){
                    dialogBoxDelete(listView.getItemAtPosition(position).toString());
                }
            }
        });
    }

    private void initViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
        userId = authViewModel.getAuthenticationId();
    }


    private void setListAndCheckBoxInInit() {
        userViewModel.getUser(userId);
        userViewModel
                .getSelectedUserLiveData()
                .observe(
                        this,
                        user -> {
                            if (user != null) {
                                setAllNotification(user.getAllNotificationActivate());
                                Log.i("Notif", "User bool activate = " + user.getAllNotificationActivate());
                                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user.getSpeciesNotifications());
                                listView.setAdapter(adapter);
                            }
                            checkBox.setChecked(user.getAllNotificationActivate());
                        }
                );
    }

    void changeBooleanAllNotification(){
        userViewModel.getUser(userId);
        userViewModel
			.getSelectedUserLiveData()
			.observe(
				this,
				user -> {
					if (user != null) {
                        userViewModel.insertUser(user);
						userViewModel
							.getInsertedUserLiveData()
							.observe(
								this,
								databaseUser -> {
									if (databaseUser != null) {
										databaseUser.setAllNotificationActivate(getAllNotification());
										userViewModel.insertUser(databaseUser);
									}
								}
							);
					}
				}
			);
    }

    private void addItemNotificationForUser(String speciesChoosed){
        userViewModel.getUser(userId);
        userViewModel
                .getSelectedUserLiveData()
                .observe(
                        this,
                        user -> {
                            if (user != null) {
                                userViewModel.insertUser(user);
                                userViewModel
                                        .getInsertedUserLiveData()
                                        .observe(
                                                this,
                                                databaseUser -> {
                                                    if (databaseUser != null) {
                                                        ArrayList<String> notif = databaseUser.getSpeciesNotifications();
                                                        notif.add(speciesChoosed);
                                                        databaseUser.setSpeciesNotifications(notif);
                                                        userViewModel.insertUser(databaseUser);
                                                    }
                                                }
                                        );
                                Log.i("Notif", "List in user après ajout = " + user.getSpeciesNotifications());
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

    public Boolean notificationActivate(String nameSpecies){
        AtomicBoolean notifActivate = new AtomicBoolean(false);
        userViewModel.getUser(userId);
        userViewModel
            .getSelectedUserLiveData()
            .observe(
                this,
                user -> {
                    if (user != null) {
                        for(String name : user.getSpeciesNotifications()){
                            if(name.equals(nameSpecies)){
                                notifActivate.set(true);
                            }
                        }
                    }
                }
            );
        return getAllNotification()||notifActivate.get();
    }

    void deleteBird(String speciesName){
            userViewModel.getUser(userId);
            userViewModel
                    .getSelectedUserLiveData()
                    .observe(
                            this,
                            user -> {
                                if (user != null) {
                                    userViewModel.insertUser(user);
                                    userViewModel
                                            .getInsertedUserLiveData()
                                            .observe(
                                                    this,
                                                    databaseUser -> {
                                                        if (databaseUser != null) {
                                                            databaseUser.deleteItemToSpeciesNotifications(speciesName);
                                                            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseUser.getSpeciesNotifications());
                                                            listView.setAdapter(adapter);
                                                            Log.i("Notif", "List in user après suppression = " + user.getSpeciesNotifications());
                                                        }
                                                    }
                                            );
                                }
                            }
                    );
        }

        private void dialogBoxDelete(String speciesName){
            new AlertDialog.Builder(this)
                    .setTitle("Suppression d'une notification")
                    .setMessage("Voulez-vous vraiment ne plus recevoir de notification pour l'espèce " + speciesName + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(NotificationActivity.this, "Notification pour l'oiseau " + speciesName + " est supprimée.", Toast.LENGTH_SHORT).show();
                            deleteBird(speciesName);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

}
