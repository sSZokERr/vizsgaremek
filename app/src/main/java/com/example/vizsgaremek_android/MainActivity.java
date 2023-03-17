package com.example.vizsgaremek_android;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageLogo;
    private Animation fadeIn;
    private Button registerButton, logInButton, searchButton;
    private TextView test;

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

        registerButton.setOnClickListener(v -> {

        });

    }


    public void init(){
        imageLogo = findViewById(R.id.imageLogo);
        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInButton);
        searchButton = findViewById(R.id.searchButton);
        test = findViewById(R.id.test);
    }
}