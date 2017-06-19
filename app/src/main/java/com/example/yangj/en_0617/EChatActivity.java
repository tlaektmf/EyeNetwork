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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class EChatActivity extends AppCompatActivity {
   // private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();//
  //  private DatabaseReference databaseReference=firebaseDatabase.getReference();//
    ListView listView;
    EditText editText;
    Button sendButton;
    String userName;
    ArrayAdapter adapter;
    String email;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset={"우리는","지금","전기프","열코딩중"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echat);

        //데이터 송수신
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url

             email = user.getEmail();//사용자 이메일을 받아오는 함수

        }

        //listView=(ListView)findViewById(R.id.listView);

       // adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1);
        //listView.setAdapter(adapter);

        editText=(EditText) findViewById(R.id.editText);

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


        Button btnFinish=(Button)findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼을 누르면 이 아래에 있는 코드를 실행을 하시오

                finish();

            }
        });

         /*
        보내기 버튼을 눌렀을 경우
         */
        Button btnSend=(Button)findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(EChatActivity.this,"제발 되어라",Toast.LENGTH_SHORT).show();

                String stText=editText.getText().toString();
                if(stText.equals("")||stText.isEmpty()){
                    Toast.makeText(EChatActivity.this,"내용을 입력해 주세요요",Toast.LENGTH_SHORT).show();
                }
                else{
                    //데이터가 입력되었을 때, 데이터베이스에 전송

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("dot array");//트리에서 어느부분에 저장함?
            myRef.setValue(stText);
                    Toast.makeText(EChatActivity.this,email+","+stText,Toast.LENGTH_SHORT).show();
                }

            }
        });





    }//oncreate의끝



}
