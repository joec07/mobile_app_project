package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.RatingForm;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeerReviewActivity extends AppCompatActivity {
    private ImageButton backButton;
    private TextView peerName;
    private RatingBar peerReviewRating;
    private EditText peerComment;
    private Button peerReviewSubmitButton;
    private ProgressBar progressBar;
    private UserForGroup receiver;
    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_review);

        receiver = (UserForGroup) getIntent().getSerializableExtra("receiver");
        groupID = getIntent().getStringExtra("groupID");

        peerName = findViewById(R.id.peerName);
        peerName.setText(receiver.getName());

        peerReviewRating = findViewById(R.id.ratePeer);
        peerComment = findViewById(R.id.commentPeer);
        peerReviewSubmitButton = findViewById(R.id.submitReview);

        backButton = findViewById(R.id.goBack);

        progressBar = findViewById(R.id.progressBar);

        peerReviewSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {submitPeerReview();}
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private String getPeerName(){
//        Intent intent = this.getIntent();
//        String pname = intent.getStringExtra("PeerName");
//        return pname;
//    }

    private void submitPeerReview(){
        float rating = peerReviewRating.getRating();
        String comment = peerComment.getText().toString();

        if(rating < 1 || comment.trim().length() == 0){
            Toast.makeText(PeerReviewActivity.this, "Failed: " + "You need to input all information", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("rating: " + rating + "\n" + "comment: " + comment);

//        POST the rating and comment to the database

        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        Call<User> call = apiInterface.postSubmitRating(new RatingForm(groupID, rating, comment, receiver.get_id()), sharedPreferences.getString("cookie", ""));
        peerReviewSubmitButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400) {
                    Toast.makeText(PeerReviewActivity.this, "Error: The information provided is not correct", Toast.LENGTH_SHORT).show();
                }  else if (response.code() == 401) {
                    Toast.makeText(PeerReviewActivity.this, "Error: You need to login first", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 200) {
                    sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    Toast.makeText(PeerReviewActivity.this, "submit successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressBar.setVisibility(View.GONE);
                peerReviewSubmitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PeerReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}