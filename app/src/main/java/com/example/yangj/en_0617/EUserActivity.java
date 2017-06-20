package com.example.yangj.en_0617;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by USER on 2017-06-20.
 */

public class EUserActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

    }


    public void EUserStartCheck(View view) {
        //시작버튼을 누를시
    }

    public void EUserDeleteCheck(View view) {
        //중단버튼을 누를시
    }
}
