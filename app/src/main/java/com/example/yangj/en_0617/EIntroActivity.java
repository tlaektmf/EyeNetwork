package com.example.yangj.en_0617;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by yangj on 2017-06-17.
 */

public class EIntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

          /*
        ID: button
        을 누르면
        로그인 엑티비티로 전환
         */
        Button btnToLogin=(Button)findViewById(R.id.button) ;
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(EIntroActivity.this,"잘들어가짐",Toast.LENGTH_SHORT).show();//잘되는지 텍스트 아웃

                //자 이제 인텐트를 넘겨줘야됨

                Intent in=new Intent(EIntroActivity.this,ELoginActivity.class);

                startActivity(in);

            }
        });

          /*
        ID: btnJoin
        을 누르면
        회원가입 엑티비티로 전환
         */
        Button btnJoin=(Button)findViewById(R.id.btnJoin) ;
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //자 이제 인텐트를 넘겨줘야됨
                Intent in2=new Intent(EIntroActivity.this,EJoinActivity.class);
                startActivity(in2);

            }
        });



    }

    public void ENLogin(View view){
        Intent intent = new Intent(getApplicationContext(), ELoginActivity.class);
        startActivity(intent);
    }

    public void ENJoinUser(View view){
        Intent intent = new Intent(getApplicationContext(), EJoinUserActivity.class);
        startActivity(intent);
    }

    public void ENJoinProtector(View view){
        Intent intent = new Intent(getApplicationContext(), EChatActivity.class);
        startActivity(intent);
    }
}
