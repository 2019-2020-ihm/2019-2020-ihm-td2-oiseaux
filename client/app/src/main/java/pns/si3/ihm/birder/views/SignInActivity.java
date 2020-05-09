package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import etudes.fr.demoosm.R;

public class SignInActivity extends AppCompatActivity {
	/**
	 * The firebase authentication instance.
	 */
	private FirebaseAuth auth;

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

		// Initialize the firebase authentication.
		auth = FirebaseAuth.getInstance();

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
						onSignInSuccess();
					}


					// Sign in failed.
					else {
						onSignInFail();
					}
				}
			});
	}

	/**
	 * Method used when sign in succeeds.
	 */
	private void onSignInSuccess() {
		// Reset password.
		this.password.setText("");

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
