package com.example.yangj.en_0617;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by USER on 2017-06-20.
 */

public class FirstFragement extends Fragment{

    ImageButton button;
    public FirstFragement(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout=(RelativeLayout)inflater.inflate(R.layout.activity_ejoin,container,false);

        button=(ImageButton)layout.findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //버튼 클릭하는 것만 봅니당..호호..(수정ㅇㅅㅇ)
                //여기서만 버튼 처리 해주세용.. Fragment는 매우예민행ㅠ
                //회원가입창입니다.
                Toast.makeText(getActivity(),"버튼 클릭",Toast.LENGTH_SHORT).show();

            }
        });
        return layout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
}
