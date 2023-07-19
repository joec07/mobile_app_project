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
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp7506_testing.API.ApiInterface;
import com.example.comp7506_testing.API.RetrofitClient;
import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.GroupForUser;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private List<Group> groupList;
    private ListView groupListView;
    private TextView noRecordTextView;
    private Context context;
    private ArrayList<Map<String, Object>> AllGroupList = new ArrayList<Map<String, Object>>();
    private User user;

    private List<Group> allGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_group_tab, container, false);


        context = view.getContext();
        searchBar = view.findViewById(R.id.myGroup_searchBar);
        groupListView = view.findViewById(R.id.myGroup_ListView);
        noRecordTextView = view.findViewById(R.id.myGroup_noRecordTextView);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", 0);
        user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);
        allGroup = new Gson().fromJson(sharedPreferences.getString("group", ""), new TypeToken<List<Group>>(){}.getType());
        List<Group> tempoAllGroup = new ArrayList<Group>();

        for(Group group: allGroup){
            if(user.getEmail().compareTo(group.getCreator().getEmail()) == 0){
                tempoAllGroup.add(group);
            }else {
                for (UserForGroup userInList: group.getList()){
                    if (userInList.get_id().compareTo(user.getId()) == 0){
                        tempoAllGroup.add(group);
                        break;
                    }
                }
            }
        }

        allGroup = tempoAllGroup;

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra("group", (Group) map.get("group"));
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

        groupList = allGroup;

        if (groupList.size() == 0){
            groupListView.setVisibility(View.GONE);
            noRecordTextView.setVisibility(View.VISIBLE);
        }else{
            System.out.println("groupList: " + groupList);
            processGroupList(groupList, true);
        }

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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseCode", currentGp.getCourseCode());
            map.put("maxNum", "" + (currentGp.getList().length + 1) + "/" + currentGp.getMaxNum());
            map.put("idea", currentGp.getIntroduction());
            map.put("createAt", new SimpleDateFormat("dd-MM-yyyy").format(currentGp.getCreateAt()));
            if (user.getEmail().compareTo(currentGp.getCreator().getEmail()) == 0){
                map.put("creatorInfo", "You");
            }else {
                map.put("creatorInfo", currentGp.getCreator().getName() + " (" + currentGp.getCreator().getEmail() + ")");
            }
            map.put("group", currentGp);
            list.add(map);
        }

        setListView(list);
    }

    private void setListView(ArrayList<Map<String, Object>> list){
        SimpleAdapter adapter = new SimpleAdapter(context,
                list,  // array list
                R.layout.my_group_list_item, // layout file
                new String[]{"courseCode", "maxNum", "idea", "createAt", "creatorInfo"}, // key in the array list
                new int[]{R.id.group_courseCode, R.id.group_maxNum, R.id.group_idea, R.id.group_createAt, R.id.group_creatorInfo} ); // id of the textview

        groupListView.setAdapter(adapter);
    }
}