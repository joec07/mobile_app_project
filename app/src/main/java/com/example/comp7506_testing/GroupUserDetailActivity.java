package com.example.comp7506_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.RatingGiven;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;

import java.text.SimpleDateFormat;

import java.lang.Integer;

public class GroupUserDetailActivity extends AppCompatActivity {

    private TextView username, major, introduction, email, rating, noComment;
    private ImageButton goBack;
    private LinearLayout emailLayout, commentLayout;

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
        rating = findViewById(R.id.groupUserDetail_rating);
        commentLayout = findViewById(R.id.groupUserDetail_commentLayout);
        noComment = findViewById(R.id.groupUserDetail_noComment);

        if (user.getRatingGiven().length == 0){
            rating.setText("--/5.0");
            commentLayout.setVisibility(View.GONE);
            noComment.setVisibility(View.VISIBLE);

        }else {
            float ratingSum = 0;
            for(RatingGiven ratingGiven: user.getRatingGiven()){
                System.out.println("ratingGiven: "+ ratingGiven);

                ratingSum += ratingGiven.getRating();

                LinearLayout parent = new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 5, 0, 5);
                parent.setLayoutParams(params);
                parent.setOrientation(LinearLayout.VERTICAL);

                TextView comment = new TextView(getBaseContext());
                comment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                comment.setText(ratingGiven.getComment());
                comment.setTextColor(Color.parseColor("#36454F"));
                comment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                comment.setTypeface(Typeface.DEFAULT_BOLD);

                TextView createAt = new TextView(getBaseContext());
                createAt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                createAt.setText(new SimpleDateFormat("dd-MM-yyyy").format(ratingGiven.getCreateAt()));
                createAt.setTextColor(Color.parseColor("#36454F"));
                createAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                createAt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                View underline = new TextView(getBaseContext());
                underline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics())));
                underline.setBackgroundColor(Color.parseColor("#7C7B7B"));

                parent.addView(comment);
                parent.addView(createAt);
                parent.addView(underline);

                commentLayout.addView(parent);
            }
//            System.out.println("score: " + ratingSum + " " + user.getRatingGiven().length + " " + ratingSum/user.getRatingGiven().length);
            rating.setText("" + Math.round((ratingSum/user.getRatingGiven().length) * 10.0) / 10.0  + "/5.0");

        }


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