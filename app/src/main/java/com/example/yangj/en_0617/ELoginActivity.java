package com.example.yangj.en_0617;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.daum.android.map.MapActivity;

/**
 * Created by yangj on 2017-06-17.
 */

public class ELoginActivity extends AppCompatActivity {

    //추가
    String TAG="ELoginActivity";
    //추가
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //밖에다가 선언
    EditText etEmail2;
    EditText etPassword2;

    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //추가
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        /*
        edit text에서 로그인을 했을 때
        서버에 등록하도록 함
         */
        etEmail2=(EditText)findViewById(R.id.editText2);
        etPassword2=(EditText)findViewById(R.id.editText3);

        pbLogin=(ProgressBar)findViewById(R.id.pbLogin);

        /*
        버튼을 눌렀을 경우 로그인 창->로그인 됐는지 확인
         */
        ImageButton btnLogin=(ImageButton)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼을 누르면 이 아래에 있는 코드를 실행을 하시오
                //Toast.makeText(ELoginActivity.this,"Login잘됨",Toast.LENGTH_SHORT).show();//잘되는지 텍스트 아웃

                pbLogin.setVisibility(View.VISIBLE);

                String  stEmail2=etEmail2.getText().toString();

                String  stPassword2=etPassword2.getText().toString();

                userLogin(stEmail2,stPassword2);

                //자 이제 인텐트를 넘겨줘야됨//->로그인 success시 이동하도록 위치 변경
               Intent in=new Intent(ELoginActivity.this,EUserActivity.class);
                startActivity(in);


            }
        });


    }

    //밖에 추가

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //메소드 추가
    public  void userLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                    //자 이제 인텐트를 넘겨줘야됨//->로그인 success시 이동하도록 위치 변경
                        Toast.makeText(ELoginActivity.this,"Authentication success.",Toast.LENGTH_SHORT).show();


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(ELoginActivity.this,"Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }

                        else{

                            //로그인이 성공 했다면 intent를 넘겨준다다
                           Intent in=new Intent(ELoginActivity.this,MapViewActivity.class/*EChatActivity.class*/);
                            startActivity(in);
                            pbLogin.setVisibility(View.GONE);

                        }

                        // ...
                    }
                });
    }
}
