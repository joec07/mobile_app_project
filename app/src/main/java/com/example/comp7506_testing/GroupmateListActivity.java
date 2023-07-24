package com.example.comp7506_testing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.Rating;
import com.example.comp7506_testing.Model.RatingGiven;
import com.example.comp7506_testing.Model.User;
import com.example.comp7506_testing.Model.UserForGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupmateListActivity extends AppCompatActivity {

    ArrayList< Map<String, Object> > AllGroupMateList = new ArrayList<Map<String, Object>>();
    private ListView groupMateListView;
    private TextView noRecordTextView;
    private ImageButton backButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmate_list);

        backButton = findViewById(R.id.goBack);

        groupMateListView = findViewById(R.id.groupmate_ListView);
        noRecordTextView = findViewById(R.id.groupmate_noRecordTextView);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", 0);
        user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);
        List<UserForGroup> userList= (List<UserForGroup>) getIntent().getSerializableExtra("groupList");
        String groupID = getIntent().getStringExtra("groupID");

        AllGroupMateList = getGroupMateList(userList, groupID);

        if (AllGroupMateList.size() == 0){
            groupMateListView.setVisibility(View.GONE);
            noRecordTextView.setVisibility(View.VISIBLE);
        }else{
            setListView(AllGroupMateList);

            groupMateListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View  view, int position, long id)
                {
                    System.out.println("position" + position + ", id" + id);
                    System.out.println(AllGroupMateList.get(position).get("PeerName"));

//                  String pname = AllGroupMateList.get(position).get("PeerName").toString();

                    Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                    // go to peer review activity
                    Intent intent = new Intent(getBaseContext(), PeerReviewActivity.class);
//                  intent.putExtra("PeerName", pname);
                    intent.putExtra("groupID", groupID);
                    intent.putExtra("receiver", (UserForGroup) map.get("receiver"));
                    startActivity(intent);
                }
            });
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Get the groupmate data from database
    private ArrayList<Map<String, Object>> getGroupMateList(List<UserForGroup> userList, String groupID){
        ArrayList< Map<String, Object> > TempList = new ArrayList<Map<String, Object>>();

        for(UserForGroup userForGroup: userList){
            if(userForGroup.get_id().compareTo(user.getId()) == 0) continue;
            boolean hasRated = false;
            for(Rating rating: user.getRating()){
                if(rating.getGroupID().compareTo(groupID) == 0 && rating.getReceiver().compareTo(userForGroup.get_id()) == 0){
                    hasRated = true;
                    break;
                }
            }
            if (hasRated) continue;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "PeerName", userForGroup.getName() );
            map.put("receiver", userForGroup);
            if (userForGroup.getRatingGiven().length == 0){
                map.put( "PeerRating",  "--/5.0");
            }else {
                int rating = 0;
                for(RatingGiven ratingGiven: userForGroup.getRatingGiven()){
                    rating += ratingGiven.getRating();
                }
                map.put( "PeerRating", "" + Math.round((rating/userForGroup.getRatingGiven().length) * 10.0) / 10.0  + "/5.0");
            }
            TempList.add(map);
        }

//        ArrayList< Map<String, Object> > TempList = new ArrayList<Map<String, Object>>();
//        ArrayList<String> peerName = new ArrayList<String>();
//        ArrayList<String> peerRating = new ArrayList<String>();
//        //*************Sample Data*************//
//        peerName.add("peer1"); peerRating.add("4.0/5.0");
//        peerName.add("peer2"); peerRating.add("5.0/5.0");
//        peerName.add("peer3"); peerRating.add("3.0/5.0");
//        peerName.add("pee4"); peerRating.add("1.0/5.0");
//        //*************Sample Data*************//
//        for( int i = 0; i < peerName.size(); i++ ){
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put( "PeerName", peerName.get(i) );
//            map.put( "PeerRating", peerRating.get(i) );
//            TempList.add(map);
//        }

        return TempList;
    }

    private void setListView(ArrayList<Map<String, Object>> list){
        SimpleAdapter adapter = new SimpleAdapter(this,
                list,
                R.layout.groupmate_list_item,
                new String[]{"PeerName", "PeerRating"},
                new int[]{R.id.peerName, R.id.peerRating});

        groupMateListView.setAdapter(adapter);
    }
}