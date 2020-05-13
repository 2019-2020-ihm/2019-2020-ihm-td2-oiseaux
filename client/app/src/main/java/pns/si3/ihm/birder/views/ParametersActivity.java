package pns.si3.ihm.birder.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import etudes.fr.demoosm.R;

import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;

public class ParametersActivity extends AppCompatActivity {
    /**
     * The authentication view model.
     */
    private AuthViewModel authViewModel;

    /**
     * The activity fields.
     */
    private EditText editPassword;
    private EditText editConfirmPassword;

	/**
	 * The activity buttons.
	 */
	private Button buttonReturn;
    private Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
		initViewModel();
		initButtons();
    }


    private void initButtons(){
        editPassword = findViewById(R.id.editText_parameters_mdp);
        editConfirmPassword = findViewById(R.id.editText_parameters_confirm_mdp);


        // Return button.
        buttonReturn = findViewById(R.id.button_param);
        buttonReturn.setOnClickListener(v -> {
			finish();
		});

        // Submit button.
		buttonChangePassword = findViewById(R.id.button_parameters_confirm);
		buttonChangePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});
    }

    /**
     * Initializes the authentication view model that holds the data.
     */
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

	/**
	 * Submit the password change, if the form is valid.
	 */
	private void submit() {
		if (isFormValid()){
			// Update the password.
			String password = editPassword.getText().toString();
			authViewModel.updatePassword(password);

			// Request succeeded.
			authViewModel.getPasswordUpdatedLiveData()
				.observe(
					this,
					passwordChanged -> {
						if (passwordChanged) {
							// Reset password.
							editPassword.setText("");
							editConfirmPassword.setText("");

							// Success toast.
							Toast.makeText(
								this,
								"Votre mot de passe a bien été modifié.",
								Toast.LENGTH_SHORT
							).show();
						}
					}
				);

			// Request failed.
			authViewModel.getAuthenticationErrorsLiveData()
				.observe(
					this,
					error -> {
						if (error != null){
							Toast.makeText(
								this,
								"Le mot de passe n'a pas pu être modifié !",
								Toast.LENGTH_SHORT
							).show();
						}
					}
				);
		}
	}

    /**
     * Checks if the form is valid.
     * @return Whether the form is valid, or not.
     */
    private boolean isFormValid() {
    	// Get the values.
		String password = editPassword.getText().toString();
		String confirmPassword = editConfirmPassword.getText().toString();

        // Password is empty.
		if (password.isEmpty()) {
			editPassword.setError("Veuillez saisir un mot de passe.");
			editConfirmPassword.setText("");
			editPassword.requestFocus();
			return false;
		}

		// Confirm password is empty.
		if (confirmPassword.isEmpty()) {
			editConfirmPassword.setError("Veuillez resaisir votre mot de passe.");
			editConfirmPassword.requestFocus();
			return false;
		}

		// Passwords don't match.
		if (!password.equals(confirmPassword)) {
			editConfirmPassword.setError("Les mots de passe ne correspondent pas.");
			editConfirmPassword.setText("");
			editConfirmPassword.requestFocus();
			return false;
		}

        return true;
    }

}
