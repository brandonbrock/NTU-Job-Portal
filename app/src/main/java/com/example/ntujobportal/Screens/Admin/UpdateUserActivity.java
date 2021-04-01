package com.example.ntujobportal.Screens.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ntujobportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateUserActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference ref;
    private String uid;

    //variables
    private EditText email_update, password_update;
    private Button btn_update, btn_delete;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        //get users unique id
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //database link
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        //New strings
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        //Hooks
        email_update = findViewById(R.id.email_update);
        password_update = findViewById(R.id.password_update);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

        email_update.setText(email);
        password_update.setText(password);

        //update button method
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //defining email and password as strings
                email = email_update.getText().toString();
                password = password_update.getText().toString();
                //validation before updating
                if (email.isEmpty()) {
                    email_update.setError("Email can't be empty!");
                    email_update.requestFocus();
                } else if (password.isEmpty()) {
                    password_update.setError("Email can't be empty!");
                    password_update.requestFocus();
                } else {
                    updateUser();
                }
            }
        });

        //delete button method
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.isEmpty()) {
                    email_update.setError("Email can't be empty!");
                    email_update.requestFocus(); //focus on specific view called
                } else if (password.isEmpty()) {
                    password_update.setError("Password can't be empty!");
                    password_update.requestFocus();
                } else {
                    deleteUser();
                }
            }
        });

    }

    private void updateUser() {
        HashMap hp = new HashMap();
        hp.put("email", email);
        hp.put("password", password);
        ref.child(uid).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateUserActivity.this, "Updated", Toast.LENGTH_SHORT);
                Intent intent = new Intent(UpdateUserActivity.this, UserAccountsActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUserActivity.this, "Error", Toast.LENGTH_SHORT);
                Intent intent = new Intent(UpdateUserActivity.this, UserAccountsActivity.class);
                startActivity(intent);
            }
        });
    }

    //delete user method
    private void deleteUser() {
        ref.child(uid).removeValue()
                .addOnCompleteListener(new OnCompleteListener < Void > () {
                    @Override
                    public void onComplete(@NonNull Task < Void > task) {
                        Toast.makeText(UpdateUserActivity.this, "Deleted", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(UpdateUserActivity.this, UserAccountsActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUserActivity.this, "Error", Toast.LENGTH_SHORT);
            }
        });
    }
}