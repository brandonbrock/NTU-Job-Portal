package com.example.ntujobportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ntujobportal.Screens.Admin.AdminActivity;
import com.example.ntujobportal.Screens.User.DashboardActivity;
import com.example.ntujobportal.Screens.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final int student = 0;
    private static final int admin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Account check
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //get user unique id
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //database root
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference uidRef = rootRef.child("Users").child(uid);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //check to see if member exists first
                    if (!dataSnapshot.exists()) {
                        Log.d("TAG", "Member does not exist!");
                    } else {
                        Log.d("TAG", "Member exists!");
                        //check account type to direct to correct activity
                        if (dataSnapshot.child("isAdmin").getValue(Long.class) == student) {
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        } else if (dataSnapshot.child("isAdmin").getValue(Long.class) == admin) {
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("TAG", databaseError.getMessage());
                }
            };
            uidRef.addListenerForSingleValueEvent(valueEventListener);
        } else {
            //no one logged in
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}