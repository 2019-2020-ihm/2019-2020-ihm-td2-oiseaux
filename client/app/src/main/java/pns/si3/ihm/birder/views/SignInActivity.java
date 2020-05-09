package pns.si3.ihm.birder.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.User;

public class SignInActivity extends AppCompatActivity {
	/**
	 * The firebase authentication instance.
	 */
	private FirebaseAuth auth;

	/**
	 * The firebase database instance.
	 */
	private FirebaseFirestore database;

	/**
	 * The email field.
	 */
	private EditText email;

	/**
	 * The password field.
	 */
	private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// Set the layout.
		setContentView(R.layout.activity_sign_in);

		// Initialize firebase.
		auth = FirebaseAuth.getInstance();
		database = FirebaseFirestore.getInstance();

		// Initialize the fields.
		email = findViewById(R.id.edit_email);
		password = findViewById(R.id.edit_password);

		// Initialize the buttons.
		initButtons();
    }

	@Override
	public void onStart() {
		super.onStart();

		// User is already connected.
		if (auth.getCurrentUser() != null) {
			// Close the sign in activity.
			 finish();
		}
	}

	/**
	 * Initializes the buttons.
	 */
	private void initButtons() {
		// Get the buttons.
    	Button returnButton = findViewById(R.id.button_return);
		final Button submitButton = findViewById(R.id.button_submit);
    	TextView signUpButton = findViewById(R.id.text_sign_up);

    	// Return button.
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Close the sign in activity.
				finish();
			}
		});

		// Submit button.
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Submit the form.
				submit();
			}
		});

		// Sign up button.
		signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Submits the form.
	 */
	private void submit() {
		// The form is valid.
		if (isValid()) {
			// Get form values.
			String emailValue = email.getText().toString();
			String passwordValue = password.getText().toString();

			// Sign in.
			signIn(emailValue, passwordValue);
		}
	}

	/**
	 * Validates the form.
	 * @return Whether the sign in form is valid, or not.
	 */
	private boolean isValid() {
		// Get form values.
		String emailValue = email.getText().toString();
		String passwordValue = password.getText().toString();

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
	 * Signs in a user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	private void signIn(String email, String password) {
		// Try to sign in.
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					// Sign in success.
					if (task.isSuccessful()) {
						// Get current user.
						FirebaseUser currentUser = auth.getCurrentUser();
						if (currentUser != null) {
							// Get current user data.
							database
								.collection("users")
								.document(currentUser.getUid())
								.get()
								.addOnSuccessListener(
									new OnSuccessListener<DocumentSnapshot>() {
										@Override
										public void onSuccess(DocumentSnapshot documentSnapshot) {
											User user = documentSnapshot.toObject(User.class);
											if (user != null) {
												onSignInSuccess(user);
											}
										}
									}
								)
								.addOnFailureListener(
									new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											// Sign in failed.
											onSignInFail();
										}
									}
								);
						}
					} else {
						// Sign in failed.
						onSignInFail();
					}
				}
			});
	}

	/**
	 * Method used when sign in succeeds.
	 */
	private void onSignInSuccess(User user) {
		// Reset password.
		this.password.setText("");

		// Success toast.
		Toast.makeText(
			SignInActivity.this,
			"Bienvenue " + user.firstName + " " + user.lastName + " !",
			Toast.LENGTH_SHORT
		).show();

		// Save user data to preferences.
		SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("firstName", user.firstName);
		editor.putString("lastName", user.lastName);
		editor.putString("email", user.email);
		editor.apply();

		// Close the sign in activity.
		finish();
	}

	/**
	 * Method used when sign in fails.
	 */
	private void onSignInFail() {
		// Reset password.
		password.setText("");
		password.requestFocus();

		// Error messages.
		password.setError("Les identifiants de connexion sont incorrects.");

		// Error toast.
		Toast.makeText(
			SignInActivity.this,
			"La connexion a échouée.",
			Toast.LENGTH_SHORT
		).show();
	}
}
