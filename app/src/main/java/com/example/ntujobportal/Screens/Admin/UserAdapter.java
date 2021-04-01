package com.example.ntujobportal.Screens.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntujobportal.Models.Users;
import com.example.ntujobportal.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter < UserAdapter.UserViewAdapter > {
    private List <Users> list; //store user model in a list to display
    private Context context;

    public UserAdapter(List < Users > list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false);
        return new UserViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewAdapter userViewAdapter, int position) {
        Users item = list.get(position);
        userViewAdapter.user_email.setText(item.getEmail());
        userViewAdapter.user_password.setText(item.getPassword());
        userViewAdapter.btn_user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserActivity.class);
                intent.putExtra("email", item.getEmail());
                intent.putExtra("password", item.getPassword());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewAdapter extends RecyclerView.ViewHolder {

        //Variables
        private TextView user_email, user_password;
        private Button btn_user_update;

        public UserViewAdapter(@NonNull View view) {
            super(view);
            //get hooks
            user_email = view.findViewById(R.id.user_email);
            user_password = view.findViewById(R.id.user_password);
            btn_user_update = view.findViewById(R.id.btn_user_update);
        }
    }
}