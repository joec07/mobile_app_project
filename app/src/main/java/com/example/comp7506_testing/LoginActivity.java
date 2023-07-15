package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;

    private ProgressBar progressBar;

    private final ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);

        // if the cookie does not expire, go to home page directly
        if (sharedPreferences.getString("cookie", "").length() > 0 && new Date(sharedPreferences.getString("cookie", "").split(";")[2].split("=")[1]).compareTo(new Date()) == 1){
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }

        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        loginButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        String tokenId = authResult.getUser().getUid();
                                        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
                                        Call<User> call = apiInterface.postLogin(User.registerOrLogin(tokenId));
                                        call.enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                Log.e(TAG, "onResponse: " + response.code());
                                                if (response.code() == 401){
                                                    Toast.makeText(LoginActivity.this, "Login Failed: " + "Invalid username or password", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    loginButton.setVisibility(View.VISIBLE);
                                                } else if (response.code() == 200){
                                                    System.out.println("BODY" + response.body());
                                                    sharedPreferences.edit().putString("cookie", response.headers().get("set-cookie")).apply();
                                                    Intent intent;
                                                    if (response.body().getIntroduction().compareTo("") == 0){
                                                        intent = new Intent(getBaseContext(), RegisterDetailActivity.class);
                                                        intent.putExtra("email", email);

                                                    }else {
                                                        sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
                                                        intent = new Intent(getBaseContext(), HomeActivity.class);
                                                    }
                                                    startActivity(intent);
                                                    finish();
                                                }


                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(LoginActivity.this, "Login Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        loginButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassword.setError("Empty fields are not allowed");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Empty fields are not allowed");
                } else {
                    loginEmail.setError("Please enter correct email");
                }
            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}