package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.GroupForUser;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;
import com.example.comp7506_testing.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView courseCode, groupSize, idea, createAt, creator, noMemberText;
    private ListView memberList;
    private Button joinButton, quitButton, notificationButton;

    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private ProgressBar progressBar;
    private User user;
    private Context context = this;
    private ImageButton goBack;

    private LinearLayout creatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);


        System.out.println("groupDetailActivity");

        Group group = (Group) getIntent().getSerializableExtra("group");

        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);

        courseCode = findViewById(R.id.groupDetail_courseCode);
        groupSize = findViewById(R.id.groupDetail_maxNum);
        idea = findViewById(R.id.groupDetail_idea);
        createAt = findViewById(R.id.groupDetail_createAt);
        creator = findViewById(R.id.groupDetail_creatorInfo);
        memberList = findViewById(R.id.groupDetail_groupList);
        joinButton = findViewById(R.id.join_button);
        progressBar = findViewById(R.id.progressBar);
        goBack = findViewById(R.id.goBack);
        creatorLayout = findViewById(R.id.creatorLayout);
        noMemberText = findViewById(R.id.noMemberText);
        quitButton = findViewById(R.id.quit_button);
        notificationButton = findViewById(R.id.notification_button);


        courseCode.setText(group.getCourseCode());
        groupSize.setText(String.valueOf(group.getList().length + 1) + "/" + String.valueOf(group.getMaxNum()));
        idea.setText(group.getIntroduction());
        createAt.setText(new SimpleDateFormat("dd-MM-yyyy").format(group.getCreateAt()));

        if (user.getEmail().compareTo(group.getCreator().getEmail()) == 0) {
            creator.setText("You");
        } else {
            creator.setText(group.getCreator().getName() + " (" + group.getCreator().getEmail() + ")");
        }


        // if the creator is the user
        if (group.getCreator().get_id().compareTo(user.getId()) == 0) {
            joinButton.setVisibility(View.GONE);
            notificationButton.setVisibility(View.VISIBLE);
        }else {
            // check if the user is in the group. If yes, show quit button. Otherwise, show join button.
            boolean isInGroup = false;
            for (String groupID : user.getGroup()) {
                if (groupID.compareTo(group.get_id()) == 0) {
                    quitButton.setVisibility(View.VISIBLE);
                    joinButton.setVisibility(View.GONE);
                    isInGroup = true;
                    break;
                }
            }

            if(!isInGroup && group.getList().length + 1 == group.getMaxNum()){
                joinButton.setVisibility(View.GONE);
                notificationButton.setVisibility(View.VISIBLE);
            }
        }


        // no member -> show the noMember text
        if (group.getList().length == 0) {
            noMemberText.setVisibility(View.VISIBLE);
            memberList.setVisibility(View.GONE);
        }


        processUserList(Arrays.asList(group.getList()));

        memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getBaseContext(), GroupUserDetailActivity.class);
                intent.putExtra("user", (UserForGroup) map.get("user"));
                startActivity(intent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        creatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GroupUserDetailActivity.class);
                intent.putExtra("user", group.getCreator());
                startActivity(intent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup(group.get_id());
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitGroup(group.get_id());
            }
        });


    }

    private void processUserList(List<UserForGroup> userList) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        System.out.println("userList size: " + userList.size());
        // first time -> initialize AllGroupList (list: point the memory location of AllGroupList -> all updates would be applied to AllGroupList)

        for (UserForGroup userinGroup : userList) {
            System.out.println("for user: " + userinGroup);
            Map<String, Object> map = new HashMap<String, Object>();
            if (userinGroup.get_id().compareTo(user.getId()) == 0) {
                map.put("detailItem_username", "You");
            } else {
                map.put("detailItem_username", userinGroup.getName());
            }
            map.put("user", userinGroup);
            list.add(map);
        }

        ViewGroup.LayoutParams layoutParams = memberList.getLayoutParams();
        layoutParams.height = 80 * userList.size();
        memberList.setLayoutParams(layoutParams);


        System.out.println("list: " + list);

        SimpleAdapter adapter = new SimpleAdapter(context,
                list,  // array list
                R.layout.group_detail_list_item, // layout file
                new String[]{"detailItem_username"}, // key in the array list
                new int[]{R.id.detailItem_username}); // id of the textview

        memberList.setAdapter(adapter);
    }

    private void joinGroup(String groupID) {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        Call<User> call = apiInterface.postJoinGroup(Group.joinOrQuitGroup(groupID), sharedPreferences.getString("cookie", ""));
        joinButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400) {
                    Toast.makeText(GroupDetailActivity.this, "The group is full", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 200) {
                    sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
                    ;
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    Toast.makeText(GroupDetailActivity.this, "join successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressBar.setVisibility(View.GONE);
                joinButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(GroupDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quitGroup(String groupID) {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        Call<User> call = apiInterface.postQuitGroup(Group.joinOrQuitGroup(groupID), sharedPreferences.getString("cookie", ""));
        quitButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 400) {
                    Toast.makeText(GroupDetailActivity.this, "The group is full", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 200) {
                    sharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
                    ;
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    Toast.makeText(GroupDetailActivity.this, "quit successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressBar.setVisibility(View.GONE);
                quitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(GroupDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}