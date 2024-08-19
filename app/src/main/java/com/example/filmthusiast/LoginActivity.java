package com.example.filmthusiast;

import static android.content.ContentValues.TAG;

import com.google.firebase.auth.UserProfileChangeRequest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private SignInButton googleBtn;
    private Button loginBtn;
    private EditText emailEdittext;
    private EditText passwordEdittext;
    private TextView actionText;
    private TextView orSign;
    private TextView nameLabel;
    private EditText nameT;

    private String singinAction = "signin";

    private FirebaseAuth mAuth;
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) transactToMainActivity(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleBtn = findViewById(R.id.google_button);
        loginBtn = findViewById(R.id.loginBtn);
        emailEdittext = findViewById(R.id.editTextEmailAddress);
        passwordEdittext = findViewById(R.id.editTextPassword);
        nameLabel = findViewById(R.id.nameText);
        nameT = findViewById(R.id.editTextName);

        actionText = findViewById(R.id.title);
        orSign = findViewById(R.id.orSign);
        mAuth = FirebaseAuth.getInstance();
        updateUiForSignInAction();

        orSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(singinAction.equals("signin")){
                    singinAction = "signup";
                    nameT.setVisibility(View.VISIBLE);
                    nameLabel.setVisibility(View.VISIBLE);
                }else{
                    singinAction = "signin";
                    nameT.setVisibility(View.GONE);
                    nameLabel.setVisibility(View.GONE);
                }
                updateUiForSignInAction();
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailEdittext.getText());
                String password = String.valueOf(passwordEdittext.getText());
                String name = String.valueOf(nameT.getText());

                boolean isValid = isEmailAndPasswordValid(email, password) && !name.isEmpty();

                if (isValid) {
                    if(singinAction.equals("signup")) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (user != null) {
                                                // Update user profile to add display name
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name)  // Add the name here
                                                        .build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User profile updated.");
                                                                    // Save user data to the database
                                                                    createUserData(user.getUid(), user.getDisplayName(), user.getEmail());
                                                                    // Transition to the main activity
//                                                                    transactToMainActivity(user);
                                                                    Toast.makeText(LoginActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                                    orSign.performClick();
                                                                } else {
                                                                    Log.w(TAG, "User profile update failed.", task.getException());
                                                                    Toast.makeText(LoginActivity.this, "Profile update failed.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }else {
                                                Toast.makeText(LoginActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed: This user may already exist", Toast.LENGTH_SHORT).show();
                                            //transactToMainActivity(null);
                                        }
                                    }
                                });

                    }
                    else{
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(LoginActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                                            transactToMainActivity(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                }


            }
        });

    }

    private void updateUiForSignInAction(){
        if(singinAction.equals("signin")){
            actionText.setText("Login");
            loginBtn.setText("Login");
            orSign.setText("or Signup");

        }else{
            actionText.setText("Sign up");
            loginBtn.setText("Sign up");
            orSign.setText("or Login");
        }
    }

    private void transactToMainActivity(FirebaseUser user) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    private void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.tv__1_)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            createUserData(user.getUid(), user.getDisplayName(), user.getEmail());
            transactToMainActivity(user);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    // Method to check the validity of email and password and show a Toast message if not valid
    public boolean isEmailAndPasswordValid( String email, String password) {
        Context context = LoginActivity.this;
        if (!isEmailValid(email)) {
            Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(context, "Password must be at least 6 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Method to check the validity of the email
    private static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to check the validity of the password (6-digit)
    private static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }


    public static void createUserData(String userId, String name, String email) {
        // Create user data
        User user = new User(userId, name, email);

        // Write user data to 'users' node
        DatabaseReference userRef = database.getReference("users").child(userId);
        userRef.setValue(user);



        System.out.println("User data created successfully.");
    }
}

class User {
    public String user_id;
    public String name;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String user_id, String name, String email) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
    }
}