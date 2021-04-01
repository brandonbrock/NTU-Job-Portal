package com.example.ntujobportal.Screens.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.Preference;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //CardView
    private CardView card_scan, card_calendar, card_logout, card_home, card_profile, card_nhs;

    //NavigationBar
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //defining cards
        card_logout = (CardView) findViewById(R.id.card_logout);
        card_home = (CardView) findViewById(R.id.card_home);
        card_profile = (CardView) findViewById(R.id.card_profile);
        card_nhs = (CardView) findViewById(R.id.card_nhs);
        card_calendar = (CardView) findViewById(R.id.card_calendar);
        card_scan = (CardView) findViewById(R.id.card_scan);

        //onclick listener
        card_logout.setOnClickListener((android.view.View.OnClickListener) this);
        card_home.setOnClickListener((android.view.View.OnClickListener) this);
        card_profile.setOnClickListener((android.view.View.OnClickListener) this);
        card_nhs.setOnClickListener((android.view.View.OnClickListener) this);
        card_calendar.setOnClickListener((android.view.View.OnClickListener) this);
        card_scan.setOnClickListener((android.view.View.OnClickListener) this);

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
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
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

    @Override
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