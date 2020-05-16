package pns.si3.ihm.birder.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.reports.MainActivity;

public class ParametersActivity extends AppCompatActivity {

    /**
     * The auth view model.
     */
    private AuthViewModel authViewModel;

    /**
     * The user view model.
     */
    private UserViewModel userViewModel;

    /**
     * The buttons of the activity.
     */
    private Button buttonChangePassword;
    private Button buttonDeleteAccount;
    private Button buttonReturn;

    /**
     * The fields of the activity.
     */
    private TextView textUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        initViewModels();
        initButtonsAndFields();
    }

    /**
     * Initializes the view models that hold the data.
     */
    private void initViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void initButtonsAndFields(){
        buttonChangePassword = findViewById(R.id.change_password);
        buttonDeleteAccount = findViewById(R.id.delete_account);
        buttonReturn = findViewById(R.id.buttonParamsRetour);
        textUser = findViewById(R.id.textUser);

        buttonDeleteAccount.setOnClickListener(v -> {
            dialogBox();
            deleteAccount();
        });

        buttonChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(ParametersActivity.this, PasswordActivity.class));
        });

        buttonReturn.setOnClickListener(v -> {
            finish();
        });

        userViewModel.getUser(authViewModel.getAuthenticationId());
        userViewModel.getSelectedUserLiveData()
                .observe(this,
                        selectedUser -> {
                    if(selectedUser != null){
                        textUser.setText("Connecté, "+ selectedUser.getFirstName() + " " + selectedUser.getLastName());
                    }
                        });
    }

    private void dialogBox(){
        new AlertDialog.Builder(this)
                .setTitle("Suppression du compte")
                .setMessage("Voulez-vous vraiment supprimer votre compte ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteAccount())
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteAccount(){
        authViewModel.deleteUser();
        authViewModel.getUserDeletedLiveData()
                .observe(this, userDelected -> {
                    if(userDelected != null){
                        Toast.makeText(this, "Compte supprimé !", Toast.LENGTH_SHORT).show();
                    }
                });
        authViewModel.signOut();
        startActivity(new Intent(ParametersActivity.this, MainActivity.class).putExtra("userDeleted", "True"));
    }

}
