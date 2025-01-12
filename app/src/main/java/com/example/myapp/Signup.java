package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText editTextEmail,editTextPassword,editTextName;
    Button signupbutton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private static final String TAG = "Signup";


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        signupbutton = findViewById(R.id.signupbutton);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Signup.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Signup.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Signup.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("name", name);
                                        userData.put("email", email);
                                        db.collection("users").document(user.getUid())
                                                .set(userData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Signup.this, "Account Created",
                                                                    Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Signup.this, "Failed to store user data",
                                                                    Toast.LENGTH_SHORT).show();
                                                            Log.e(TAG, "Failed to store user data", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    if (task.getException() != null) {
                                        Toast.makeText(Signup.this, "Authentication failed: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Authentication failed", task.getException());
                                    } else {
                                        Toast.makeText(Signup.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });



        TextView loginTextView = findViewById(R.id.loginTextView);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Signup.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}