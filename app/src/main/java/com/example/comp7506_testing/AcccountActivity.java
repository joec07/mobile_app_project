package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcccountActivity extends AppCompatActivity {

    private TextView username;
    private Button logoutButton;
    private Button updateAccountButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private final ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acccount);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);

        username = findViewById(R.id.account_username);
        logoutButton = findViewById(R.id.logout_button);
        updateAccountButton = findViewById(R.id.update_account_button);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.goBack);

        username.setText(new Gson().fromJson(sharedPreferences.getString("user", ""), User.class).getName());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        updateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AccountUpdateActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });


    }

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        logoutButton.setVisibility(View.GONE);
        updateAccountButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<String> call = apiInterface.postLogout(sharedPreferences.getString("cookie", ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400){
                    Toast.makeText(AcccountActivity.this, "Logout Failed: " + "please try again later", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                    updateAccountButton.setVisibility(View.VISIBLE);
                } else if (response.code() == 200){
                    sharedPreferences.edit().remove("cookie").apply();
                    sharedPreferences.edit().remove("user").apply();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AcccountActivity.this, "Logout Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}