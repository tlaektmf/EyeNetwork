package com.example.yangj.en_0617;

import android.content.Intent;
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

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

//생성 test
public class EChatActivity extends AppCompatActivity {
    ListView listView;
    EditText editText;
    Button sendButton;
    String userName;
    ArrayAdapter adapter;
    String email;
    List<EdataReadWrite> mComments;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //String[] myDataset={"우리는","지금","전기프","열코딩중"};
    FirebaseDatabase database;
    @Override



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echat);

         database = FirebaseDatabase.getInstance();//instance를 받아야돼
        //데이터 송수신

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//현재 user정보를 가지고 옴
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
        mComments=new ArrayList<>();
        mAdapter = new MyAdapter(mComments);
        mRecyclerView.setAdapter(mAdapter);


       //final String readMe=email;
        DatabaseReference myRef = database.getReference(user.getUid());//읽어올 트리 헤드 이름(값이 미리 존재해야 읽을 수 있음)



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                EdataReadWrite comment = dataSnapshot.getValue(EdataReadWrite.class);



                                     // [START_EXCLUDE]
                                     // Update RecyclerView

                                     mComments.add(comment);//list를 만들어줌
                                     mAdapter.notifyItemInserted(mComments.size() - 1);//아이템이 들어가면 반영한다
                                  // [END_EXCLUDE]

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

      //있으면 편리함//로그인 화면으로 돌아가짐(시각장애인의 끝내기 버튼)
        Button btnFinish=(Button)findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼을 누르면 이 아래에 있는 코드를 실행을 하시오

                finish();

            }
        });

        /*
        점찍기 위한 버튼 이벤트
         */
        Button btnGoToMap=(Button)findViewById(R.id.btnGoToMap);
        btnGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //자 이제 인텐트를 넘겨줘야됨

                Intent in=new Intent(EChatActivity.this,EPointOutActivity.class);

                startActivity(in);
            }
        });



         /*
        보내기 버튼을 눌렀을 경우
         */
         //데이터 등록
        Button btnSend=(Button)findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(EChatActivity.this,"제발 되어라",Toast.LENGTH_SHORT).show();

                String stText=editText.getText().toString();
                if(stText.equals("")||stText.isEmpty()){
                    Toast.makeText(EChatActivity.this,"내용을 입력해 주세요요",Toast.LENGTH_SHORT).show();
                }
                else{
                    //데이터가 입력되었을 때, 데이터베이스에 전송

           // FirebaseDatabase database = FirebaseDatabase.getInstance();//instance를 받아야돼

           // DatabaseReference myRef = database.getReference("dot array");//트리 헤드 이름

                    //헤드의 child를 데이터를 수신한 시간으로 설정함
                    //head dotarray에 날짜별로 데이터가 올라가짐
                    Calendar c =Calendar.getInstance();
                    SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    String formattedDate=df.format((c.getTime()));

                    DatabaseReference myRef = database.getReference(user.getUid()).child(formattedDate);//트리 헤드 이름


                    //해쉬테이블 이용합니당
                    Hashtable<String, String> dataDot
                            = new Hashtable<String, String>();
                    dataDot.put("UserID", email);//key, value(내용)//key값이름 바꾸려면 저기도 반드시 같이 수정해줘야됨
                    dataDot.put("sendedText", stText);
                    dataDot.put("구분", "보호자");//데이터베이스 저장소에는 보호자만 되어있음
                                                    //사용자는 저장된 데이터만 읽어서 가져옴


            myRef.setValue(dataDot);
                    //Toast.makeText(EChatActivity.this,email+","+stText,Toast.LENGTH_SHORT).show();
                }

            }
        });





    }//oncreate의끝





}
