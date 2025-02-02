package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName, mEmail, mPassword, mConfirmpass;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mConfirmpass = findViewById(R.id.confirmpass);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String fullName = mFullName.getText().toString();
                String confirmPass = mConfirmpass.getText().toString();

                if (TextUtils.isEmpty(fullName)){
                    mFullName.setError("full name is required");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("password is required");
                    return;
                }
                if (password.length() < 6){
                    mPassword.setError("Password Must be >= 6 characters");
                    return;
                }
                if (confirmPass.isEmpty() || !password.equals(confirmPass)){
                    mConfirmpass.setError("Invalid password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "user create", Toast.LENGTH_SHORT).show();
                            startActivitysecond();

                            // send verification code
                            FirebaseUser fuser = fAuth.getCurrentUser();

                            fuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Signup.this, "Verification Email has been Sent", Toast.LENGTH_SHORT).show();
                                    String userid = fuser.getUid();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure:Email not sent" + e.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(Signup.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private  void startActivitysecond(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("newUser");

        String name = mFullName.getText().toString();
        String email = mEmail.getText().toString();
        String username = mConfirmpass.getText().toString();

        String password = mPassword.getText().toString();
        String android = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        String userAuId = fAuth.getUid();

        String role = "user";
        int id = 0;


        HelperClass helperClass = new HelperClass(name,email,password,android,userAuId,id,role);
        reference.child(userAuId).setValue(helperClass);
        start();
    }

    private void start(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("newUser");

        String name = mFullName.getText().toString();
        String email = mEmail.getText().toString();
        String username = mConfirmpass.getText().toString();

        String password = mPassword.getText().toString();
        String android = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        String userAuId = fAuth.getUid();

        String role = "user";
        int id = 0;

        HelperClass helperClass = new HelperClass(name,email,password,android,userAuId,id,role);
        reference.child(userAuId).setValue(helperClass);

        Intent intent = new Intent(Signup.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}