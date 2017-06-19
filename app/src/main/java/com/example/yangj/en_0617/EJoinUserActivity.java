package com.example.yangj.en_0617;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by USER on 2017-06-20.
 */

public class EJoinUserActivity extends AppCompatActivity{
    ViewPager vp;
    TextToSpeech tts;
    boolean ttsActive=false;
    int mCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);
        vp=(ViewPager)findViewById(R.id.vp);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(1);
        vp.setOnPageChangeListener((new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition=position;
                switch (position){
                    case 0:
                        Toast.makeText(getApplicationContext(), "회원가입창", Toast.LENGTH_SHORT).show();
                        String text="회원가입창입니다. 이메일과 비밀번호를 입력해주십시오.";
                        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "로그인창", Toast.LENGTH_SHORT).show();
                        String text2="로그인창입니다. 이메일과 비밀번호를 입력해주십시오.";
                        tts.speak(text2,TextToSpeech.QUEUE_FLUSH,null);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }));


        tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);

                }
            }
        });

    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new FirstFragement();
                case 1:
                    return new SecondFragement();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }


    }

}
