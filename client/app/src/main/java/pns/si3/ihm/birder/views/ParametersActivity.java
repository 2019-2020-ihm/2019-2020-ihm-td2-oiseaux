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
        authViewModel.updatePassword(password);
        authViewModel.getPasswordUpdatedLiveData()
                .observe(
                        this,
                        value -> {
                            if(value){
                                // Success toast.
                                Toast.makeText(
                                        this,
                                        "Votre mot de passe a bien été changé !",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
        authViewModel.getAuthenticationErrorsLiveData()
                .observe(
                        this,
                        error -> {
                            if(error != null){
                                Toast.makeText(
                                        this,
                                        "Le mot de passe n'a pas pu être changé.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


}
