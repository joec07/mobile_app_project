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
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyGroupTab extends Fragment {

    private EditText searchBar;
    private GroupForUser[] groupList;
    private ListView groupListView;
    private TextView noRecordTextView;
    private Context context;
    private ArrayList<Map<String, Object>> AllGroupList = new ArrayList<Map<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_group_tab, container, false);


        context = view.getContext();
        searchBar = view.findViewById(R.id.myGroup_searchBar);
        groupListView = view.findViewById(R.id.myGroup_ListView);
        noRecordTextView = view.findViewById(R.id.myGroup_noRecordTextView);

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

                List<GroupForUser> FilteredGroupList = new ArrayList<>();

                for (GroupForUser group : groupList) {
                    if (group.getCourseCode().toLowerCase().contains(text.toLowerCase())) {
                        FilteredGroupList.add(group);
                    }
                }

                processGroupList(FilteredGroupList, false);

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", 0);
        groupList = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class).getGroup();

        if (groupList.length == 0){
            groupListView.setVisibility(View.GONE);
            noRecordTextView.setVisibility(View.VISIBLE);
        }else{
            List<GroupForUser> GroupListArrList = Arrays.asList(groupList);
            System.out.println("groupList: " + groupList);
            processGroupList(GroupListArrList, true);
        }

        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void processGroupList(List<GroupForUser> updatedGrouplist, boolean isFirstTime){
        ArrayList<Map<String, Object>> list;
        // first time -> initialize AllGroupList (list: point the memory location of AllGroupList -> all updates would be applied to AllGroupList)
        if (isFirstTime){
            list = AllGroupList;
        }else{
            list = new ArrayList<Map<String, Object>>();
        }

        for (int i = 0; i < updatedGrouplist.size(); i++) {
            GroupForUser currentGp = updatedGrouplist.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseCode", currentGp.getCourseCode());
            map.put("maxNum", currentGp.getMaxNum());
            map.put("idea", currentGp.getIntroduction());
            map.put("creatAt", new SimpleDateFormat("dd-MM-yyyy").format(currentGp.getCreateAt()));
            list.add(map);
        }

        setListView(list);
    }

    private void setListView(ArrayList<Map<String, Object>> list){
        SimpleAdapter adapter = new SimpleAdapter(context,
                list,  // array list
                R.layout.my_group_list_item, // layout file
                new String[]{"courseCode", "maxNum", "idea", "creatAt"}, // key in the array list
                new int[]{R.id.group_courseCode, R.id.group_maxNum, R.id.group_idea, R.id.group_createAt} ); // id of the textview

        groupListView.setAdapter(adapter);
    }
}