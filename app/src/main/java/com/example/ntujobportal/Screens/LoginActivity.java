package com.example.ntujobportal.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.Admin.AdminActivity;
import com.example.ntujobportal.Screens.Admin.RegistrationActivity;
import com.example.ntujobportal.Screens.User.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email_login, password_login;

    private Button btn_login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        Login();
    }

    private void Login() {
        //get hooks
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);

        //login button method
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ensure email and password is string and no whitespaces get entered
                String email = email_login.getText().toString().trim();
                String password = password_login.getText().toString().trim();

                //validation for empty inputs
                if (TextUtils.isEmpty(email)) {
                    email_login.setError("Email is invalid!");
                    return;
                }
                //validation for empty inputs
                if (TextUtils.isEmpty(password)) {
                    password_login.setError("Password is invalid!");
                    return;
                }

                //firebase login method
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener < AuthResult > () {
                    @Override
                    public void onComplete(@NonNull Task < AuthResult > task) {
                        if (task.isSuccessful()) {
                            //get user unique id
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //database root
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference uidRef = rootRef.child("Users").child(uid);
                            //event listener for authentication
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //check to see if any data exists and account type
                                    if (dataSnapshot.exists()) {
                                        Log.d("TAG", "user exists!");
                                        if (dataSnapshot.child("isAdmin").getValue(Long.class) == 0) {
                                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                        } else if (dataSnapshot.child("isAdmin").getValue(Long.class) == 1) {
                                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                        }
                                    } else {
                                        Log.d("TAG", "User does not exists!");
                                        //dialog box opens to let user know details are incorrect
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                        alertDialog.setTitle("Login Authentication");
                                        alertDialog.setMessage("User Account does not exist!");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("TAG", databaseError.getMessage());
                                }
                            };
                            uidRef.addListenerForSingleValueEvent(valueEventListener);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}