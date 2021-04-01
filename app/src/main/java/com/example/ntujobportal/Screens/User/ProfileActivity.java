package com.example.ntujobportal.Screens.User;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.Admin.UpdateUserActivity;
import com.example.ntujobportal.Screens.Admin.UserAccountsActivity;
import com.example.ntujobportal.Screens.LoginActivity;
import com.example.ntujobportal.Screens.Preference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //NavigationBar
    private DrawerLayout drawer;

    //Firebase
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get users unique id
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //database link
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation
        drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_bar);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //nav header
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get email to display in navigation
        if (user != null) {
            String email = user.getEmail();
            //get hooks
            navigationView.setNavigationItemSelectedListener(this);
            TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email);
            txtProfileName.setText(email);
        }

        //profile page txt
        auth = FirebaseAuth.getInstance();
        String email = user.getEmail();
        TextView welcomeText = findViewById(R.id.welcometxt);
        welcomeText.setText("Welcome : " + email);

        //logout button
        Button logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        //reset password button
        Button resetPassword = findViewById(R.id.btn_resetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        //delete account button
        Button deleteAccount = findViewById(R.id.btn_deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    public void deleteAccount() {
        ref.child(uid).removeValue()
                .addOnCompleteListener(new OnCompleteListener < Void > () {
                    @Override
                    public void onComplete(@NonNull Task < Void > task) {
                        Toast.makeText(ProfileActivity.this, "Deleted", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, Preference.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_jobs:
                intent = new Intent(this, JobPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                signOut();
                break;
            case R.id.nav_share:
                share();
                break;
            case R.id.nav_rate:
                rate();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Calendar() {
        Intent intent;
        intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void rate() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClick(View v) {
        Intent intent;
        Uri uriUrl = Uri.parse("https://covid19.nhs.uk/");
        switch (v.getId()) {
            case R.id.card_logout:
                signOut();
                break;
            case R.id.card_home:
                intent = new Intent(this, JobPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.card_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.card_nhs:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(browserIntent);
                break;
            case R.id.card_calendar:
                Calendar();
                break;
            case R.id.card_scan:
                intent = new Intent(this, QRActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}