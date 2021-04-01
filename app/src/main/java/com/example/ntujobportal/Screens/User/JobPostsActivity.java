package com.example.ntujobportal.Screens.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.ntujobportal.MainActivity;
import com.example.ntujobportal.Models.JobPost;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.Preference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JobPostsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //NavigationBar
    private DrawerLayout drawer;

    //Recycler View
    private RecyclerView recyclerView;

    //Firebase
    private DatabaseReference JobPostDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        //Database Link
        JobPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        //Job Posts
        recyclerView = findViewById(R.id.recycler_job_post_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions < JobPost > firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder < JobPost > ()
                        .setQuery(JobPostDatabase, JobPost.class)
                        .build();

        FirebaseRecyclerAdapter < JobPost, MyViewHolder > adapter = new FirebaseRecyclerAdapter < JobPost, MyViewHolder > (firebaseRecyclerOptions) {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post, parent, false);
                return new MyViewHolder(view);
            }

            protected void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i, @NonNull JobPost model) {

                viewHolder.setJobTitle(model.getJobTitle());
                viewHolder.setJobType(model.getJobType());
                viewHolder.setJobSalary(model.getJobSalary());
                viewHolder.setJobDegree(model.getJobDegree());
                viewHolder.setJobLocation(model.getJobLocation());
                viewHolder.setJobDeadline(model.getJobDeadline());
                viewHolder.setJobWebsite(model.getJobWebsite());
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        View myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setJobTitle(String title) {

            TextView jobTitle = myView.findViewById(R.id.job_title);
            jobTitle.setText(title);
        }

        public void setJobType(String type) {
            TextView jobType = myView.findViewById(R.id.job_type);
            jobType.setText(type);
        }

        public void setJobSalary(String salary) {
            TextView jobSalary = myView.findViewById(R.id.job_salary);
            jobSalary.setText(salary);
        }

        public void setJobDegree(String degree) {
            TextView jobDegree = myView.findViewById(R.id.job_degree);
            jobDegree.setText(degree);
        }

        public void setJobLocation(String location) {
            TextView jobLocation = myView.findViewById(R.id.job_location);
            jobLocation.setText(location);
        }

        public void setJobDeadline(String deadline) {
            TextView jobDeadline = myView.findViewById(R.id.job_deadline);
            jobDeadline.setText(deadline);
        }

        public void setJobWebsite(String website) {
            TextView jobWebsite = myView.findViewById(R.id.job_website);
            jobWebsite.setText(website);
        }
    }
}