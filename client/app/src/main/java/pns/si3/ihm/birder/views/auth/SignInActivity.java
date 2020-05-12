package pns.si3.ihm.birder.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;

public class SignInActivity extends AppCompatActivity {
	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "SignInActivity";

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
	private EditText editEmail;
	private EditText editPassword;

	/**
	 * The activity buttons.
	 */
	private Button returnButton;
	private Button submitButton;
	private TextView signUpButton;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		initViewModel();
		initFields();
		initButtons();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (authViewModel.isAuthenticated()) {
			finish();
		}
	}

	/**
	 * Initializes the authentication view model that holds the data.
	 */
	private void initViewModel() {
		authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
		userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
	}

	/**
	 * Initializes the activity fields.
	 */
	private void initFields() {
		editEmail = findViewById(R.id.edit_email);
		editPassword = findViewById(R.id.edit_password);
	}

	/**
	 * Initializes the activity buttons.
	 */
	private void initButtons() {
    	// Return button.
		returnButton = findViewById(R.id.button_return);
		returnButton.setOnClickListener(v -> {
			finish();
		});

		// Submit button.
		submitButton = findViewById(R.id.button_submit);
		submitButton.setOnClickListener(v -> {
			signIn();
		});

		// Sign up button.
		signUpButton = findViewById(R.id.text_sign_up);
		signUpButton.setOnClickListener(v -> {
			Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
			startActivity(intent);
		});
	}

	/**
	 * Signs in the user.
	 */
	private void signIn() {
		if (isFormValid()) {
			signInWithEmailAndPassword();
		}
	}

	/**
	 * Signs in the user with an email and password.
	 */
	private void signInWithEmailAndPassword() {
		// Get values.
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();

		Log.i(TAG, "Sign in with email and password.");
		authViewModel.signInWithEmailAndPassword(email, password);

		// Auth succeeded.
		authViewModel
			.getAuthenticatedUserLiveData()
			.observe(
				this,
				authUser -> {
					if (authUser != null) {
						Log.i(TAG, "Auth succeeded.");
						getUserFromDatabase(authUser.id);
					}
				}
			);

		// Auth failed.
		authViewModel
			.getAuthenticationErrorsLiveData()
			.observe(
				this,
				authError -> {
					if (authError != null) {
						Log.e(TAG, "Auth failed.");
						Log.e(TAG, authError.getMessage());

						// Reset password.
						editPassword.setText("");
						editPassword.requestFocus();

						// Error messages.
						editPassword.setError("Les identifiants de connexion sont incorrects.");

						// Error toast.
						Toast.makeText(
							SignInActivity.this,
							"La connexion a échouée.",
							Toast.LENGTH_SHORT
						).show();

						// Clears the authentication error.
						authViewModel.clearAuthenticationError();
					}
				}
			);
	}

	/**
	 * Gets the user from the database.
	 * @param id The id of the user.
	 */
	private void getUserFromDatabase(String id) {
		Log.i(TAG, "Get user from the database.");
		userViewModel.getUser(id);

		// Query succeeded.
		userViewModel
			.getUserLiveData()
			.observe(
				this,
				databaseUser -> {
					if (databaseUser != null) {
						Log.i(TAG, "Query succeeded.");

						// Reset password.
						editPassword.setText("");

						// Success toast.
						Toast.makeText(
							this,
							"Bonjour " + databaseUser.firstName + " !",
							Toast.LENGTH_LONG
						).show();

						// Close the activity.
						finish();
					}
				}
			);

		// Query failed.
		userViewModel
			.getUserErrorsLiveData()
			.observe(
				this,
				databaseError -> {
					if (databaseError != null) {
						Log.e(TAG, "Query failed.");
						Log.e(TAG, databaseError.getMessage());

						// Reset password.
						editPassword.setText("");
						editPassword.requestFocus();

						// Error toast.
						Toast.makeText(
							SignInActivity.this,
							"Une erreur est survenue.",
							Toast.LENGTH_SHORT
						).show();

						// Clear the database error.
						userViewModel.clearUserErrors();
					}
				}
			);
	}

	/**
	 * Checks if the form is valid.
	 * @return Whether the form is valid, or not.
	 */
	private boolean isFormValid() {
		// Get values.
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();

		// Email is empty.
		if (email.isEmpty()) {
			editEmail.setError("Veuillez saisir une adresse email.");
			editEmail.requestFocus();
			return false;
		}

		// Email is invalid.
		if (!isEmailValid(email)) {
			editEmail.setError("Veuillez saisir une adresse email valide.");
			editEmail.requestFocus();
			return false;
		}

		// Password is empty.
		if (password.isEmpty()) {
			editPassword.setError("Veuillez saisir un mot de passe.");
			editPassword.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Checks if an email is valid.
	 * @param email The email to be checked.
	 * @return Whether the email is valid, or not.
	 */
	boolean isEmailValid(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
