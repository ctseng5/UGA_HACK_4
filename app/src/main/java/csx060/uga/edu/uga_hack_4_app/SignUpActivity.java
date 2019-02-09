package csx060.uga.edu.uga_hack_4_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    //Initialize global variables
    private Button signUpButton;
    private Button loginButton;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView passwordConfirmation;
    private TextView firstName;
    private TextView lastName;
    private TextView phoneNum;
    private TextView passwordMessage;
    private FirebaseAuth auth;
    //Reference to Firebase Database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Reference to Information table
    DatabaseReference ref = database.getReference("Information");

    /**
     * Creates the views for the signup activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase Auth instance
        auth = FirebaseAuth.getInstance();

        //Create views
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setEnabled(false);
        signUpButton.setOnClickListener(new SignUpOnClickListener());
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginOnClickListener());
        emailInput = findViewById(R.id.emailField);
        passwordInput = findViewById(R.id.passwordField);
        passwordConfirmation = findViewById(R.id.confirmPasswordField);
        passwordConfirmation.addTextChangedListener(new PasswordListener());
        firstName = findViewById(R.id.fname);
        lastName = findViewById(R.id.lname);
        passwordMessage = findViewById(R.id.passwordMessage);


    }

    /**
     * On click listener for the login option if the user already has an account
     * Starts the LoginActivity
     */
    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity( intent );
        }
    }

    /**
     * TextWatcher to see see if the password and password confirmation fields match.
     * If they don't display a warning message
     */
    private class PasswordListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(passwordInput.getText().toString().equals(passwordConfirmation.getText().toString())) {
                passwordMessage.setText("");
                signUpButton.setEnabled(true);
            }
            else {
                passwordMessage.setText("Passwords do not match");
                signUpButton.setEnabled(false);
            }
        }
    }

    /**
     * On click listener for the sign up button
     * Clicking on button will get all the text from the fields and create the user
     */
    private class SignUpOnClickListener implements View.OnClickListener {
        public void onClick(View view) {

            final String email = emailInput.getText().toString().trim();
            final String password = passwordInput.getText().toString().trim();
            final String fname = firstName.getText().toString().trim();
            final String lname = lastName.getText().toString().trim();

            //If any fields are empty, display error toast message
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(fname)) {
                Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(lname)) {
                Toast.makeText(getApplicationContext(), "Enter your last name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * Create new user using email and password using Firebase auth
             */
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        /**
                         * If account creation is completed, add field to Firebase Authentication
                         * Also add a new database entry for user's first name, last name, email, userID,
                         * @param task
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignUpActivity.this, "Created Account:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                DatabaseReference usersRef = ref.child("users");
                                usersRef.child(auth.getUid()).setValue(new User(fname, lname, email, auth.getUid()));
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }
    }

    /**
     * On click listener for the login option if the user already has an account
     * Starts the LoginActivity
     */
    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity( intent );
        }
    }

    /**
     * TextWatcher to see see if the password and password confirmation fields match.
     * If they don't display a warning message
     */
    private class PasswordListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(passwordInput.getText().toString().equals(passwordConfirmation.getText().toString())) {
                passwordMessage.setText("");
                signUpButton.setEnabled(true);
            }
            else {
                passwordMessage.setText("Passwords do not match");
                signUpButton.setEnabled(false);
            }
        }
    }

    /**
     * On click listener for the sign up button
     * Clicking on button will get all the text from the fields and create the user
     */
    private class SignUpOnClickListener implements View.OnClickListener {
        public void onClick(View view) {

            final String email = emailInput.getText().toString().trim();
            final String password = passwordInput.getText().toString().trim();
            final String fname = firstName.getText().toString().trim();
            final String lname = lastName.getText().toString().trim();

            //If any fields are empty, display error toast message
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(fname)) {
                Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(lname)) {
                Toast.makeText(getApplicationContext(), "Enter your last name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * Create new user using email and password using Firebase auth
             */
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        /**
                         * If account creation is completed, add field to Firebase Authentication
                         * Also add a new database entry for user's first name, last name, email, userID,
                         * @param task
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignUpActivity.this, "Created Account:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                DatabaseReference usersRef = ref.child("users");
                                usersRef.child(auth.getUid()).setValue(new User(fname, lname, email, auth.getUid()));
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }
    }


}
