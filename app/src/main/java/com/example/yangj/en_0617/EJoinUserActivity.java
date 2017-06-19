package com.example.yangj.en_0617;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Locale;

/**
 * Created by yangj on 2017-06-17.
 */

public class EJoinUserActivity extends Activity {
    TextToSpeech tts;
    boolean ttsActive=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingView sv=new SlidingView(this);
        View v1=View.inflate(this, R.layout.activity_login,null);
        View v2=View.inflate(this,R.layout.activity_ejoin,null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);
        tts=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener(){
                    @Override
                    public void onInit(int status) {
                        tts.setLanguage(Locale.KOREAN);
                    }
                });
            String text="로그인 창입니다.";
            ttsActive=true;
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);


    }
}
