package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDetailActivity extends AppCompatActivity {

    private Button signupDetailsSubmitButton;
    private EditText signupName, signupIntro, signupMajor;
    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);

        signupName = findViewById(R.id.signup_name);
        signupIntro = findViewById(R.id.signup_introduction);
        signupMajor = findViewById(R.id.signup_major);
        progressBar = findViewById(R.id.progressBar);
        signupDetailsSubmitButton = findViewById(R.id.signup_detail_button);

        signupDetailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPersonalInfo();
            }
        });

    }

    private void submitPersonalInfo(){
        String name = signupName.getText().toString();
        String intro = signupIntro.getText().toString();
        String major = signupMajor.getText().toString();
        if (name.trim().length() == 0 || intro.trim().length() == 0 || major.trim().length() == 0){
            Toast.makeText(RegisterDetailActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        Call<User> call = apiInterface.postUpdateInfo(User.updateInfo(name, major, intro, getIntent().getStringExtra("email")), sharedPreferences.getString("cookie", ""));
        signupDetailsSubmitButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400){
                    Toast.makeText(RegisterDetailActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 200){
                    sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();;
                    startActivity( new Intent(getBaseContext(), HomeActivity.class));
                }
                progressBar.setVisibility(View.GONE);
                signupDetailsSubmitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterDetailActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}