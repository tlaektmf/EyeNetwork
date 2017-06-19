package com.example.yangj.en_0617;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class EChatActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    ListView listView;
    EditText editText;
    Button sendButton;
    String userName;
    ArrayAdapter adapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset={"안녕","오늘","뭐했어","영화볼래"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echat);

        listView=(ListView)findViewById(R.id.listView);
        editText=(EditText)findViewById(R.id.editText);
        sendButton=(Button)findViewById(R.id.button5);
        userName="user"+new Random().nextInt(1000);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1);
        listView.setAdapter(adapter);
          /*
        버튼을 눌렀을 경우 로그인 창->채팅 엑티비티로 전환
         */
        Button btnFinish=(Button)findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼을 누르면 이 아래에 있는 코드를 실행을 하시오

                finish();

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    databaseReference.child("Location").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationData locationData=dataSnapshot.getValue(LocationData.class);
            adapter.add(locationData.getUserName()+": "+locationData.getMessage());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }










    });







    }//oncreate


    public void EnChatClick(View view) {
        LocationData locationData=new LocationData(userName, editText.getText().toString());
        databaseReference.child("Location").push().setValue(locationData);
        editText.setText("");
    }

}
