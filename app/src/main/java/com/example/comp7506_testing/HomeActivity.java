package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.UserForGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ImageButton userInfo, addGroup;
    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private EditText searchBar;
    private Group[] groupList;
    private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private ListView groupListView;
    private ProgressBar progressBar;
    private Context context = this;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
//        System.out.println("user passed: " + user);

        userInfo = findViewById(R.id.userInfo);
        addGroup = findViewById(R.id.addGroup);
        searchBar = findViewById(R.id.searchBar);
        groupListView = findViewById(R.id.homeListView);
        progressBar = findViewById(R.id.progressBar);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddGroupActivity.class));
            }
        });

        Call<Group[]> call = apiInterface.getGroup();
        call.enqueue(new Callback<Group[]>() {
            @Override
            public void onResponse(Call<Group[]> call, Response<Group[]> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 404){
                    Toast.makeText(HomeActivity.this, "Error: please try again later", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 200){
                    groupList = response.body();
                    System.out.println("groupList: " + groupList);
                    for (int i = 0; i < groupList.length; i++) {
                        Group currentGp = groupList[i];
                        UserForGroup creator = currentGp.getCreator();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("courseCode", currentGp.getCourseCode());
                        map.put("maxNum", currentGp.getMaxNum());
                        map.put("idea", currentGp.getIntroduction());
                        map.put("creatorInfo", creator.getName() + "\nMajor: " + creator.getMajor() + "\nIntroduction: " + creator.getIntroduction() + "\nEmail: " + creator.getEmail());
                        map.put("creatAt", new SimpleDateFormat("dd-MM-yyyy").format(currentGp.getCreateAt()));
                        list.add(map);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(context,
                            list,  // array list
                            R.layout.group_list_item, // layout file
                            new String[]{"courseCode", "maxNum", "idea", "creatorInfo", "creatAt"}, // key in the array list
                            new int[]{R.id.group_courseCode, R.id.group_maxNum, R.id.group_idea, R.id.group_creatorInfo, R.id.group_createAt} ); // id of the textview

                    groupListView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    groupListView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<Group[]> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
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