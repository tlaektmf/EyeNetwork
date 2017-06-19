package com.example.yangj.en_0617;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), EIntroActivity.class);
                startActivity(intent);
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }



    @Override
    protected void onDestroy() {
        Log.i("test", "onDestroy()");
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
