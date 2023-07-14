package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.User;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupActivity extends AppCompatActivity {

    private EditText courseCode, groupSize, introduction, completionDate;
    private Button addGroupSubmitButton;
    private ImageButton backButton;
    private ProgressBar progressBar;

    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        courseCode = findViewById(R.id.add_courseCode);
        groupSize = findViewById(R.id.add_groupSize);
        introduction = findViewById(R.id.add_introduction);
        completionDate = findViewById(R.id.add_date);
        addGroupSubmitButton = findViewById(R.id.add_submit);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.goBack);

        addGroupSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    submit();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });

    }

    private void submit() {
        String form_courseCode = courseCode.getText().toString();
        String form_introduction = introduction.getText().toString();
        if (form_courseCode.trim().length() == 0 || groupSize.getText().toString().trim().length() == 0 || form_introduction.trim().length() == 0 || completionDate.getText().toString().trim().length() == 0){
            Toast.makeText(AddGroupActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            Date form_completionDate = new SimpleDateFormat("dd/MM/yyyy").parse(completionDate.getText().toString());
            int form_groupSize = Integer.parseInt(groupSize.getText().toString());

            SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
            Call<User> call = apiInterface.postAddGroup(Group.addGroup(form_courseCode, form_introduction, form_groupSize, form_completionDate), sharedPreferences.getString("cookie", ""));
            addGroupSubmitButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e(TAG, "onResponse: " + response.code());
                    if (response.code() == 400){
                        Toast.makeText(AddGroupActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        addGroupSubmitButton.setVisibility(View.VISIBLE);
                    }else if (response.code() == 200){
                        sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(AddGroupActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    addGroupSubmitButton.setVisibility(View.VISIBLE);
                }
            });
        }catch (Exception e){
            Toast.makeText(AddGroupActivity.this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}