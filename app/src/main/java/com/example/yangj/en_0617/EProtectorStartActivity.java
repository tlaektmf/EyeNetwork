package com.example.yangj.en_0617;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by USER on 2017-06-19.
 */

public class EProtectorStartActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protector);
    }

    public void ENLoginProtectorClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ELoginActivity.class);
        startActivity(intent);
    }

    public void ENjoinProtectorClick(View view) {
        Intent intent = new Intent(getApplicationContext(),  EJoinActivity.class);
        startActivity(intent);
    }
}
