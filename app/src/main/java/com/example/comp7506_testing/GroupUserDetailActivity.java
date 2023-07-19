package com.example.comp7506_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;

public class GroupUserDetailActivity extends AppCompatActivity {

    private TextView username, major, introduction, email;
    private ImageButton goBack;

    private LinearLayout emailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user_detail);

        UserForGroup user = (UserForGroup) getIntent().getSerializableExtra("user");

        username = findViewById(R.id.groupUserDetail_username);
        major = findViewById(R.id.groupUserDetail_Major);
        introduction = findViewById(R.id.groupUserDetail_Introduction);
        email = findViewById(R.id.groupUserDetail_Email);
        goBack = findViewById(R.id.goBack);
        emailLayout = findViewById(R.id.emailLayout);



        if (user.getEmail() == null){
            emailLayout.setVisibility(View.GONE);
        }else {
            email.setText(user.getEmail());
        }

        username.setText(user.getName());
        major.setText(user.getMajor());
        introduction.setText(user.getIntroduction());

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}