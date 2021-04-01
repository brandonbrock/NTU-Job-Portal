package com.example.ntujobportal.Screens.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ntujobportal.Models.Users;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    //variables
    EditText email_register, password_register, c_password_register;
    Button btn_register;
    private static final int isAdmin = 0;

    //Firebase auth
    private FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        Register();
    }

    private void Register() {
        //Hooks to all xml elements in activity_registration.xml
        email_register = (EditText) findViewById(R.id.email_register);
        password_register = (EditText) findViewById(R.id.password_register);
        c_password_register = (EditText) findViewById(R.id.c_password_register);
        btn_register = (Button) findViewById(R.id.btn_register);

        //Saving data when clicking register button
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //database link
                ref = FirebaseDatabase.getInstance().getReference().child("Users");

                //Get values
                String email = email_register.getText().toString().trim();
                String password = password_register.getText().toString().trim();
                String cPassword = c_password_register.getText().toString().trim();

                //validation for empty inputs
                if (TextUtils.isEmpty(email)) {
                    email_register.setError("Please enter an email address e.g. example@example.com");
                    return;
                }
                //validation for matching passwords
                if (TextUtils.isEmpty(password)) {
                    password_register.setError("Password cannot be empty!");
                    return;
                } else if (!cPassword.equals(password)) {
                    c_password_register.setError("Passwords don't match");
                    return;
                }

                //creating user account with Firebase--
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener < AuthResult > () {
                    @Override
                    public void onComplete(@NonNull Task < AuthResult > task) {
                        if (task.isSuccessful()) {
                            //get user unique id
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //add email, password, account type to user model
                            Users user = new Users(email, password, isAdmin);
                            ref.child(uid).setValue(user);
                            Toast.makeText(RegistrationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                        } else {
                            //dialog box opens to let user know details are incorrect
                            Log.i("Response", "Failed to create user:" + task.getException().getMessage());
                            AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this).create();
                            alertDialog.setTitle("Registration Authentication");
                            alertDialog.setMessage("User Account already exists!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
            }
        });

    }
}