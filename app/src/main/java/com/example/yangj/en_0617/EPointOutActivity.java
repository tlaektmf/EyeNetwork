package com.example.yangj.en_0617;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class EPointOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epoint_out);

        Toast.makeText(EPointOutActivity.this,"여기",Toast.LENGTH_SHORT).show();//엑티비티 바뀌는것 확인

    }
}
