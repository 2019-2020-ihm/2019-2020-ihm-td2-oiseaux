package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.auth.SignInActivity;
import pns.si3.ihm.birder.views.reports.MainActivity;
import pns.si3.ihm.birder.views.reports.MapActivity;

public class ParametersActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String userId;
    private String userEmail;
    private String firstName;
    private String lastName;

    private String password;
    private String confirmPassword;

    private Button buttonReturn;

    /**
     * The authentication view model.
     */
    private AuthViewModel authViewModel;

    /**
     * The user view model.
     */
    private UserViewModel userViewModel;

    /**
     * The activity fields.
     */
    private EditText editPassword;
    private EditText editConfirmPassword;

    private Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        init();
        initViewModel();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = editPassword.getText().toString();
                confirmPassword = editConfirmPassword.getText().toString();
                if(isFormValid()){
                    Log.i("Params", password + " and confirm " + confirmPassword);
                    tryChangePassword();
                }
            }
        });

    }


    private void init(){
        auth = FirebaseAuth.getInstance();
        editPassword = (EditText) findViewById(R.id.editText_parameters_mdp);
        editConfirmPassword = (EditText) findViewById(R.id.editText_parameters_confirm_mdp);
        buttonChangePassword = (Button) findViewById(R.id.button_parameters_confirm);
        buttonReturn = (Button) findViewById(R.id.button_param);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParametersActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initializes the authentication view model that holds the data.
     */
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userId = authViewModel.getAuthenticationId();
        userViewModel.getUser(userId);
        userViewModel
                .getSelectedUserLiveData()
                .observe(
                        this,
                        user -> {
                            if (user != null) {
                                userEmail = user.getEmail();
                                firstName = user.getFirstName();
                                lastName = user.getLastName();
                            }
                        }
                );
    }

    /**
     * Checks if the form is valid.
     * @return Whether the form is valid, or not.
     */
    private boolean isFormValid() {
        // Password is empty.
        if (password.isEmpty()) {
            editPassword.setError("Veuillez saisir un nouveau mot de passe.");
            return false;
        }

        // ConfirmPassword is empty.
        if (confirmPassword.isEmpty()) {
            editConfirmPassword.setError("Veuillez confirmer le nouveau mot de passe.");
            return false;
        }
        return password.equals(confirmPassword);
    }

    /**
     * Checks if the password is new and change it if possible.
     * @return Whether the password is valid, or not.
     */
    private void tryChangePassword() {
        String mdp = editPassword.getText().toString();
        Log.i("Params", "password = " + mdp);
        authViewModel.signInWithEmailAndPassword(userEmail, mdp);
        Log.i("Params", "in try");

        // Auth succeeded.
        authViewModel
                .getAuthenticatedUserLiveData()
                .observe(
                        this,
                        authUser -> {
                            if (authUser != null) {
                                Log.i("Params", "Auth succeed donc mdp identique à l'ancier");
                                // Reset password.
                                editPassword.setText("");
                                editConfirmPassword.setText("");

                                // Error messages.
                                editPassword.setError("Veuillez saisir un mot de passe différent de l'ancien.");

                                // Error toast.
                                Toast.makeText(
                                        ParametersActivity.this,
                                        "Le mot de passe saisie est identique à l'ancien.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );

        // Auth failed.
        authViewModel
                .getAuthenticationErrorsLiveData()
                .observe(
                        this,
                        error -> {
                            if (error != null) {
                                Log.i("Params", "Auth failed donc on peut changer le mdp");
                                // Reset password.
                                editPassword.setText("");
                                editConfirmPassword.setText("");
                                changeUserWithEmailAndPassword(password);

                            }
                        }
                );



    }

    /**
     * Change the password of a user.
     */
    private void changeUserWithEmailAndPassword(String password) {
        Log.i("Params", "Start the change");
        // TODO
        //Ici changer le mdp de l'utilisateur
    }

    /**
     * Change a user in the database.
     * @param user The user to be changed.
     */
    private void createUserInDatabase(User user) {
        userViewModel.insertUser(user);
        userViewModel
                .getInsertedUserLiveData()
                .observe(
                        this,
                        databaseUser -> {
                            if (databaseUser != null) {
                                // Success toast.
                                Toast.makeText(
                                        ParametersActivity.this,
                                        "Le mot de passe a bien été modifié.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                // Close the activity.
                                //finish();
                            }
                        }
                );
    }

}
