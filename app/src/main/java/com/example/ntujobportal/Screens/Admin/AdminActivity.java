package com.example.ntujobportal.Screens.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    //button variables
    Button btn_addJob, btn_logout, btn_create, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Menu();
    }

    private void Menu() {
        //get hooks
        btn_addJob = (Button) findViewById(R.id.btn_addJob);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_update = (Button) findViewById(R.id.btn_update);

        //add job to dashboard listing
        btn_addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InsertJobActivity.class));
            }
        });

        //create student account
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        //update user details
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, UserAccountsActivity.class));
            }
        });

        //end session
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(AdminActivity.this, MainActivity.class));
            }
        });
    }

}