package com.example.comp7506_testing;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;
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


public class AllGroupTab extends Fragment {

    private ImageButton addGroup;
    private ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private EditText searchBar;
    private Group[] groupList;
    private ListView groupListView;
    private ProgressBar progressBar;
    private Context context;
    private ArrayList<Map<String, Object>> AllGroupList = new ArrayList<Map<String, Object>>();
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_group_tab, container, false);


        context = view.getContext();
        addGroup = view.findViewById(R.id.addGroup);
        searchBar = view.findViewById(R.id.searchBar);
        groupListView = view.findViewById(R.id.allGroupListView);
        progressBar = view.findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", 0);
        user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra("group", (Group) map.get("detail"));
                System.out.println("PASSED BEFORE: " + (Group) map.get("detail"));
                startActivity(intent);
            }
        });


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString();


                // no text -> show all group
                if (text.trim().length() == 0){
                    setListView(AllGroupList);
                    return;
                }

                List<Group> FilteredGroupList = new ArrayList<>();

                for (Group group : groupList) {
                    if (group.getCourseCode().toLowerCase().contains(text.toLowerCase())) {
                        FilteredGroupList.add(group);
                    }
                }

                processGroupList(FilteredGroupList, false);

            }
        });


        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddGroupActivity.class));
            }
        });

        Call<Group[]> call = apiInterface.getGroup(sharedPreferences.getString("cookie", ""));
        call.enqueue(new Callback<Group[]>() {
            @Override
            public void onResponse(Call<Group[]> call, Response<Group[]> response) {
                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 404){
                    Toast.makeText(view.getContext(), "Error: please try again later", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 200){
                    groupList = response.body();
                    List<Group> GroupListArrList = Arrays.asList(groupList);
                    sharedPreferences.edit().putString("group", new Gson().toJson(GroupListArrList)).apply();
                    processGroupList(GroupListArrList, true);
                    progressBar.setVisibility(View.GONE);
                    groupListView.setVisibility(View.VISIBLE);
                    for(Group gp: GroupListArrList){
                        System.out.println("GP: " + gp);
                    }

                }

            }

            @Override
            public void onFailure(Call<Group[]> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void processGroupList(List<Group> updatedGrouplist, boolean isFirstTime){
        ArrayList<Map<String, Object>> list;
        // first time -> initialize AllGroupList (list: point the memory location of AllGroupList -> all updates would be applied to AllGroupList)
        if (isFirstTime){
            list = AllGroupList;
        }else{
            list = new ArrayList<Map<String, Object>>();
        }

        for (int i = 0; i < updatedGrouplist.size(); i++) {
            Group currentGp = updatedGrouplist.get(i);
            UserForGroup creator = currentGp.getCreator();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseCode", currentGp.getCourseCode());
            map.put("maxNum", "" + (currentGp.getList().length + 1) + "/" + currentGp.getMaxNum());
            map.put("idea", currentGp.getIntroduction());
            if (user.getEmail().compareTo(creator.getEmail()) == 0){
                map.put("creatorInfo", "You");
            }else {
                map.put("creatorInfo", creator.getName() + " (" + creator.getEmail() + ")");
            }
            map.put("createAt", new SimpleDateFormat("dd-MM-yyyy").format(currentGp.getCreateAt()));
            map.put("detail", currentGp);
            list.add(map);
        }

        System.out.println("list: " + list);

        setListView(list);
    }

    private void setListView(ArrayList<Map<String, Object>> list){
        SimpleAdapter adapter = new SimpleAdapter(context,
                list,  // array list
                R.layout.group_list_item, // layout file
                new String[]{"courseCode", "maxNum", "idea", "creatorInfo", "createAt"}, // key in the array list
                new int[]{R.id.group_courseCode, R.id.group_maxNum, R.id.group_idea, R.id.group_creatorInfo, R.id.group_createAt} ); // id of the textview

        groupListView.setAdapter(adapter);
    }
}