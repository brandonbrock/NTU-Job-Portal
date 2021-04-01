package com.example.ntujobportal.Screens.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ntujobportal.Models.JobPost;
import com.example.ntujobportal.R;
import com.example.ntujobportal.Screens.User.JobPostsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertJobActivity extends AppCompatActivity {

    //Variables
    EditText job_title;
    EditText job_type;
    EditText job_salary;
    EditText job_degree;
    EditText job_location;
    EditText job_deadline;
    EditText job_website;
    Button btn_postJob;

    //Database References
    DatabaseReference ref;
    JobPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job);

        //get hooks
        job_title = (EditText) findViewById(R.id.job_title);
        job_type = (EditText) findViewById(R.id.job_type);
        job_salary = (EditText) findViewById(R.id.job_salary);
        job_degree = (EditText) findViewById(R.id.job_degree);
        job_location = (EditText) findViewById(R.id.job_location);
        job_deadline = (EditText) findViewById(R.id.job_deadline);
        job_website = (EditText) findViewById(R.id.job_website);
        btn_postJob = (Button) findViewById(R.id.btn_postJob);
        post = new JobPost();

        //database link
        ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        btn_postJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if text field is empty validation otherwise post
                if (job_title.getText().toString().isEmpty()) {
                    job_title.setError("Please enter a Job Title!");
                    return;
                } else {
                    post.setJobTitle(job_title.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_type.getText().toString().isEmpty()) {
                    job_type.setError("Please enter a Job Type!");
                    return;
                } else {
                    post.setJobType(job_type.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_salary.getText().toString().isEmpty()) {
                    job_salary.setError("Please enter a Salary!");
                    return;
                } else {
                    post.setJobSalary(job_salary.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_degree.getText().toString().isEmpty()) {
                    job_degree.setError("Please enter a Degree Requirements!");
                    return;
                } else {
                    post.setJobDegree(job_degree.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_location.getText().toString().isEmpty()) {
                    job_location.setError("Please enter a Location!!");
                    return;
                } else {
                    post.setJobLocation(job_location.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_deadline.getText().toString().isEmpty()) {
                    job_deadline.setError("Please enter a Deadline for Job!");
                    return;
                } else {
                    post.setJobDeadline(job_deadline.getText().toString().trim());
                }
                //if text field is empty validation otherwise post
                if (job_website.getText().toString().isEmpty()) {
                    job_website.setError("Please enter a Website for Job!");
                    return;
                } else {
                    post.setJobWebsite(job_website.getText().toString().trim());
                }
                //once validation is complete post the job post
                ref.push().setValue(post);
                Toast.makeText(getApplicationContext(), "Job Posted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), JobPostsActivity.class));
            }
        });
    }
}