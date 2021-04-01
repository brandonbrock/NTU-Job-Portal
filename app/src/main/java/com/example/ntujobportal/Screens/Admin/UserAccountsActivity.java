package com.example.ntujobportal.Screens.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ntujobportal.Models.Users;
import com.example.ntujobportal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAccountsActivity extends AppCompatActivity {

    private RecyclerView recycler_users;
    private List < Users > list;
    private DatabaseReference ref;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);
        recycler_users = findViewById(R.id.recycler_users);
        //database link
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        users();

    }

    private void users() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list = new ArrayList < > ();
                if (!datasnapshot.exists()) {
                    //no data
                } else {
                    for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                        //get list of users from user model
                        Users user = snapshot.getValue(Users.class);
                        list.add(user);
                    }
                    recycler_users.setHasFixedSize(true);
                    recycler_users.setLayoutManager(new LinearLayoutManager(UserAccountsActivity.this));
                    adapter = new UserAdapter(list, UserAccountsActivity.this);
                    recycler_users.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}