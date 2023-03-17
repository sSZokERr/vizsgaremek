package com.example.vizsgaremek_android;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageLogo;
    private Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        VideoView videoview = (VideoView) findViewById(R.id.videoViewBackground);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background);
        videoview.setVideoURI(uri);
        videoview.start();

        imageLogo.startAnimation(fadeIn);
    }

    public void init(){
        imageLogo = findViewById(R.id.imageLogo);
        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
    }
}