package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.UserForGroup;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ImageButton userInfo;

    private ArrayList<Map<String, Object>> AllGroupList = new ArrayList<Map<String, Object>>();
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
//        System.out.println("user passed: " + user);

        userInfo = findViewById(R.id.userInfo);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }

    private void logout(){
//        String name = signupName.getText().toString();
//        String intro = signupIntro.getText().toString();
//        String major = signupMajor.getText().toString();
//        if (name.trim().length() == 0 || intro.trim().length() == 0 || major.trim().length() == 0){
//            Toast.makeText(RegisterDetailActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
//        Call<User> call = apiInterface.postUpdateInfo(User.updateInfo(name, major, intro), sharedPreferences.getString("cookie", ""));
//        signupDetailsSubmitButton.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.e(TAG, "onResponse: " + response.code());
//                if (response.code() == 400){
//                    Toast.makeText(RegisterDetailActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
//                }else if (response.code() == 200){
//                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
//                    intent.putExtra("user", response.body());
//                    startActivity(intent);
//                }
//                progressBar.setVisibility(View.GONE);
//                signupDetailsSubmitButton.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(RegisterDetailActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}