package com.example.yangj.en_0617;

import android.opengl.EGLDisplay;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EJoinActivity extends AppCompatActivity {

    //추가
    String TAG="EJoinActivity";
    //추가
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //밖에다가 선언
    EditText etEmail1;
    EditText etPassword1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejoin);

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
        ID: btnRegister : 회원가입 버튼을 눌렀을 경우
        정보입력&서버에 저장
         */
        etEmail1=(EditText)findViewById(R.id.etJoinID);
        etPassword1=(EditText)findViewById(R.id.etJoinPassword);
        Button btnRegister=(Button)findViewById(R.id.btnRegister);//등록버튼을 누른다

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stEmail=etEmail1.getText().toString();
                String stPassword=etPassword1.getText().toString();
               // Toast.makeText(EJoinActivity.this,stEmail+","+stPassword,Toast.LENGTH_SHORT).show();//잘들어옴
                if(stEmail.isEmpty()||stEmail.equals("")||stPassword.isEmpty()||stPassword.equals("")){
                    Toast.makeText(EJoinActivity.this,"등록할 아이디를 입력해 주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(stEmail,stPassword);//회원가입 버튼 누를경우 실행됨
                }


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
    public  void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        Toast.makeText(EJoinActivity.this,"register success",Toast.LENGTH_SHORT).show();



                        if (!task.isSuccessful()) {
                            Toast.makeText(EJoinActivity.this,"Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
