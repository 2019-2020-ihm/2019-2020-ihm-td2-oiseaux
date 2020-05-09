package pns.si3.ihm.birder.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.User;

public class SignUpActivity extends AppCompatActivity {
	/**
	 * The firebase authentication instance.
	 */
	private FirebaseAuth auth;

	/**
	 * The firebase database instance.
	 */
	private FirebaseFirestore database;

	/**
	 * The first name field.
	 */
	private EditText firstName;

	/**
	 * The last name field.
	 */
	private EditText lastName;

	/**
	 * The email field.
	 */
	private EditText email;

	/**
	 * The password field.
	 */
	private EditText password;

	/**
	 * The confirm password field.
	 */
	private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout.
        setContentView(R.layout.activity_sign_up);

		// Initialize firebase.
		auth = FirebaseAuth.getInstance();
		database = FirebaseFirestore.getInstance();

		// Initialize the fields.
		initFields();

		// Initialize the buttons.
		initButtons();
    }

	@Override
	public void onStart() {
		super.onStart();

		// The user is already connected.
		if (auth.getCurrentUser() != null) {
			// Close the sign up activity.
			finish();
		}
	}

	/**
	 * Initializes the fields.
	 */
	private void initFields() {
    	firstName = findViewById(R.id.edit_first_name);
		lastName = findViewById(R.id.edit_last_name);
		email = findViewById(R.id.edit_email);
		password = findViewById(R.id.edit_password);
		confirmPassword = findViewById(R.id.edit_confirm_password);
	}

	/**
	 * Initializes the buttons.
	 */
	private void initButtons() {
		// Return button.
		findViewById(R.id.button_return).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Close the sign up activity.
					finish();
				}
			}
		);

		// Submit button.
		findViewById(R.id.button_submit).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Submit the form.
					submit();
				}
			}
		);
	}

	/**
	 * Submits the form.
	 */
	private void submit() {
		// The form is valid.
		if (isValid()) {
			// Get form values.
			String firstNameValue = firstName.getText().toString();
			String lastNameValue = lastName.getText().toString();
			String emailValue = email.getText().toString();
			String passwordValue = password.getText().toString();
			String confirmPasswordValue = confirmPassword.getText().toString();

			// Sign up.
			signUp(
				firstNameValue,
				lastNameValue,
				emailValue,
				passwordValue
			);
		}
	}

	/**
	 * Validates the form.
	 * @return Whether the form is valid, or not.
	 */
	private boolean isValid() {
		// Get form values.
		String firstNameValue = firstName.getText().toString();
		String lastNameValue = lastName.getText().toString();
		String emailValue = email.getText().toString();
		String passwordValue = password.getText().toString();
		String confirmPasswordValue = confirmPassword.getText().toString();

		// First name is empty.
		if (firstNameValue.isEmpty()) {
			firstName.setError("Veuillez saisir un prénom.");
			firstName.requestFocus();
			return false;
		}

		// Last name is empty.
		if (lastNameValue.isEmpty()) {
			lastName.setError("Veuillez saisir un nom.");
			lastName.requestFocus();
			return false;
		}

		// Email is empty.
		if (emailValue.isEmpty()) {
			email.setError("Veuillez saisir une adresse email.");
			email.requestFocus();
			return false;
		}

		// Email is invalid.
		if (!isEmailValid(emailValue)) {
			email.setError("Veuillez saisir une adresse email valide.");
			email.requestFocus();
			return false;
		}

		// Password is empty.
		if (passwordValue.isEmpty()) {
			password.setError("Veuillez saisir un mot de passe.");
			password.requestFocus();
			return false;
		}

		// Confirm password is empty.
		if (confirmPasswordValue.isEmpty()) {
			confirmPassword.setError("Veuillez resaisir votre mot de passe.");
			confirmPassword.requestFocus();
			return false;
		}

		// Passwords don't match.
		if (!passwordValue.equals(confirmPasswordValue)) {
			confirmPassword.setError("Les mots de passe ne correspondent pas.");
			confirmPassword.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Validates an email address
	 * @param email The email address to be validated.
	 * @return Whether the email address is valid, or not.
	 */
	boolean isEmailValid(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	/**
	 * Signs up a user.
	 * @param firstName The first name of the user.
	 * @param lastName The last name of the user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	private void signUp(final String firstName, final String lastName, final String email, String password) {
		// Try to sign up.
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					// Sign up succeeded.
					if (task.isSuccessful()) {
						// Get the current user.
						FirebaseUser currentUser = auth.getCurrentUser();
						if (currentUser != null) {
							// Create the user.
							User user = new User(
								currentUser.getUid(),
								firstName,
								lastName,
								email
							);

							// Save the user.
							database.collection("users")
								.add(user)
								.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
									@Override
									public void onSuccess(DocumentReference documentReference) {
										// Sign up success.
										onSignUpSuccess();
									}
								})
								.addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										// Sign up failed.
										onSignUpFail();
									}
								});
						}
					} else {
						// Sign up failed.
						onSignUpFail();
					}
				}
			});
	}

	/**
	 * Method used when the sign up succeeds.
	 */
	private void onSignUpSuccess() {
		// Reset passwords.
		password.setText("");
		confirmPassword.setText("");

		// Success toast.
		Toast.makeText(
			SignUpActivity.this,
			"L'inscription a été validée.",
			Toast.LENGTH_SHORT
		).show();

		// Close the sign up activity.
		finish();
	}

	/**
	 * Method used when the sign up fails.
	 */
	private void onSignUpFail() {
		// Reset passwords.
		password.setText("");
		confirmPassword.setText("");
		password.requestFocus();

		// Error toast.
		Toast.makeText(
			SignUpActivity.this,
			"L'inscription a échouée.",
			Toast.LENGTH_SHORT
		).show();
	}
}
