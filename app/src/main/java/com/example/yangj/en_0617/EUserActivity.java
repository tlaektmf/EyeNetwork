package com.example.yangj.en_0617;

import android.app.Activity;
import android.content.Intent;
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
        setContentView(R.layout.activity_euser_choose);

    }

   public void ENProtector_Choose1(View view) {
        //길 좌표를 찍어주세요
        Intent intent = new Intent(this,LocationActivity.class);
        startActivity(intent);
    }

    public void ENProtector_Choose2(View view) {
        //현재 사용자의 위치를 알아온답니당.
        Intent intent = new Intent(this,EPointOutActivity.class);
        startActivity(intent);
    }
}
