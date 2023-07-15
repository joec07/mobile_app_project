package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountUpdateActivity extends AppCompatActivity {

    private Button updateDetailsSubmitButton;
    private EditText updateName, updateIntro, updateMajor;
    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private ProgressBar progressBar;
    private ImageButton goBackButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);

        updateName = findViewById(R.id.update_name);
        updateIntro = findViewById(R.id.update_introduction);
        updateMajor = findViewById(R.id.update_major);
        progressBar = findViewById(R.id.progressBar);
        updateDetailsSubmitButton = findViewById(R.id.update_button);
        goBackButton = findViewById(R.id.goBack);

        updateName.setText(user.getName());
        updateIntro.setText(user.getIntroduction());
        updateMajor.setText(user.getMajor());


        updateDetailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPersonalInfo();
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AcccountActivity.class));
            }
        });
    }

    private void submitPersonalInfo(){
        String name = updateName.getText().toString();
        String intro = updateIntro.getText().toString();
        String major = updateMajor.getText().toString();
        if (name.trim().length() == 0 || intro.trim().length() == 0 || major.trim().length() == 0){
            Toast.makeText(AccountUpdateActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        Call<User> call = apiInterface.postUpdateInfo(User.updateInfo(name, major, intro), sharedPreferences.getString("cookie", ""));
        updateDetailsSubmitButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400){
                    Toast.makeText(AccountUpdateActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    updateDetailsSubmitButton.setVisibility(View.VISIBLE);
                }else if (response.code() == 200){
                    sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();;
                    startActivity( new Intent(getBaseContext(), AcccountActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AccountUpdateActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}