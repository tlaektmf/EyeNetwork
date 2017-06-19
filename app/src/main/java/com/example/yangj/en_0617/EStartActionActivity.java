package com.example.yangj.en_0617;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector.SimpleOnGestureListener;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by USER on 2017-06-19.
 */

public class EStartActionActivity extends Activity{
    TextToSpeech tts;
    boolean ttsActive=false;

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureDetector=new GestureDetector(this, TestGestureListner);

        tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                    ttsActive=true;
                    String text="시각장애인이시면 두번 탭을 해주세요. 아니시면 버튼을 눌러주세요.";
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    SimpleOnGestureListener TestGestureListner=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent ev) {
            Toast.makeText(getApplicationContext(),"다운",Toast.LENGTH_SHORT);
            Log.w("TEST", "onDown=" + ev.toString());
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent ev) {

            ttsActive=true;
            String text="두번 탭하셨습니다. 로그인 창으로 이동합니다.";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
            Toast.makeText(getApplicationContext(), "두번 탭하셨습니다.",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),EStartActionActivity.class);
            startActivity(intent);
            return true;
        }
    };

    public void ENProtectorCheck(View view) {
        Intent intent=new Intent(getApplicationContext(),EProtectorStartActivity.class);
        startActivity(intent);
    }
}
