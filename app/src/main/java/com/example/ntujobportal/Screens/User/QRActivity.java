package com.example.ntujobportal.Screens.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.LoginActivity;
import com.example.ntujobportal.Screens.Preference;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //NavigationBar
    private DrawerLayout drawer;

    //Firebase
    private FirebaseAuth auth;

    //QR code
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

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
            //check email exists
            boolean emailVerified = user.isEmailVerified();
            //make sure uid matches to firebase
            String uid = user.getUid();
            //get hooks
            navigationView.setNavigationItemSelectedListener(this);
            TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email);
            txtProfileName.setText(email);
        }
        scanCode();

    }

    private void scanCode() {
        btn_scan = findViewById(R.id.btn_Scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(QRActivity.this);
                integrator.setCaptureActivity(CaptureActivity.class);
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanning Code");
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(QRActivity.this);
                alertDialog.setTitle("Scanning Result");
                alertDialog.setMessage(result.getContents());
                alertDialog.setPositiveButton("scan again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            case R.id.card_scan:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}