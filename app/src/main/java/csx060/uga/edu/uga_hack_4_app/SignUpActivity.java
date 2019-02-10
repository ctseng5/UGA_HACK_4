package csx060.uga.edu.uga_hack_4_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

//api stuff
//import com.google.android.gms.common.api.Response;
//import android.content.Entity;

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
    private static final String API_KEY = "prod-a59aa9a65739dcebd25d1d1c1621c703b22ac8c5e9bd99100cab75be443ccb1e7d6066256655505e476ab01a2385692abdd7845d40b4622bdfdccac3a52e70bf";
    private static final String API_URL = "https://certwebservices.ft.cashedge.com/sdk/Payments/Customers/";
    private static final String BSN_ID= "BUSN-88fc17bfa29dcd3facb4f24ca29683c9e14575e3d9ec77d78053423beab73cfd";
    //Reference to Firebase Database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Reference to Information table
    DatabaseReference ref = database.getReference("Information");

    /**
     * Creates the views for the signup activity.
     *
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
            view.getContext().startActivity(intent);
        }
    }

    /**
     * TextWatcher to see see if the password and password confirmation fields match.
     * If they don't display a warning message
     */
    private class PasswordListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (passwordInput.getText().toString().equals(passwordConfirmation.getText().toString())) {
                passwordMessage.setText("");
                signUpButton.setEnabled(true);
            } else {
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
                         *
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
                            } else {
                                /**
                                 * * Create user API call to Fiserv API
                                 */
                                Client client = ClientBuilder.newClient();
                                MultivaluedMap<String,Object> header= new MultivaluedHashMap<String,Object>();
                                header.add("apiKey",API_KEY);
                                header.add("businessID",BSN_ID);

                                String body = "{\n" +
                                        "\"address\": {\n" +
                                        "  \"city\": \"Lithonia\",\n" +
                                        "  \"line1\": \"1234 Maple Road\",\n" +
                                        "  \"line2\": \"\",\n" +
                                        "  \"state\": \"GA\",\n" +
                                        "  \"zip\": \"43017\"\n" +
                                        "},\n" +
                                        "\"defaultSpeed\": \"Next Day\",\n" +
                                        "\"email\": "+ email + ",\n" +
                                        "\"fundingAccount\": {\n" +
                                        "   \"ddaAccount\": {\n" +
                                        "    \"accountNumber\": \"226771203\",\n" +
                                        "    \"accountType\": \"Checking\",\n" +
                                        "    \"rtn\": \"044000037\"\n" +
                                        "  },\n" +
                                        "  \"nickName\": \"Shalissa\"\n" +
                                        "},\n" +
                                        "\"mode\": \"initiate\",\n" +
                                        "\"personName\": {\n" +
                                        " \"firstName\":" + firstName +",\n" +
                                        "  \"lastName\":"+ lastName +"\n" +
                                        "},\n" +
                                        "\"phone1\": \"6145643001\",\n" +
                                        "\"phone2\": \"6145643002\",\n" +
                                        "\"requestID\": \"testcustomerAdd2\",\n" +
                                        " \"version\": \"1\"\n" +
                                        "}";

                                Response response = client.target(API_URL)
                                        .request(MediaType.APPLICATION_JSON)
                                        .headers(header)
                                        .post(Entity.entity(body, MediaType.APPLICATION_JSON));
                                System.out.println(response.readEntity(String.class));

                                DatabaseReference usersRef = ref.child("users");
                                usersRef.child(auth.getUid()).setValue(new User(fname, lname, email, auth.getUid()));
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }//onClick
    }
}
