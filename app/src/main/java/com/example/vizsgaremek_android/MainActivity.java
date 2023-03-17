package com.example.vizsgaremek_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageLogo;
    private Animation fadeIn;
    private Button registerButton, logInButton, searchButton;
    private EditText editTextFirst, editTextSecond, editTextThird, editTextFourth;

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
            logInButton.setBackgroundColor(Color.TRANSPARENT);
            logInButton.setTextColor(Color.BLACK);
            registerButton.setBackgroundColor(Color.BLACK);
            registerButton.setTextColor(Color.WHITE);
            searchButton.setBackgroundColor(Color.TRANSPARENT);
            searchButton.setTextColor(Color.BLACK);
            editTextThird.setVisibility(View.VISIBLE);
            editTextSecond.setVisibility(View.VISIBLE);
            editTextFourth.setVisibility(View.VISIBLE);
        });

        logInButton.setOnClickListener(v -> {
            registerButton.setBackgroundColor(Color.TRANSPARENT);
            registerButton.setTextColor(Color.BLACK);
            logInButton.setBackgroundColor(Color.BLACK);
            logInButton.setTextColor(Color.WHITE);
            searchButton.setBackgroundColor(Color.TRANSPARENT);
            searchButton.setTextColor(Color.BLACK);
            editTextThird.setVisibility(View.GONE);
            editTextFourth.setVisibility(View.GONE);
        });
        searchButton.setOnClickListener(v -> {
            registerButton.setBackgroundColor(Color.TRANSPARENT);
            registerButton.setTextColor(Color.BLACK);
            logInButton.setBackgroundColor(Color.TRANSPARENT);
            logInButton.setTextColor(Color.BLACK);
            searchButton.setBackgroundColor(Color.BLACK);
            searchButton.setTextColor(Color.WHITE);
            editTextThird.setVisibility(View.GONE);
            editTextSecond.setVisibility(View.GONE);
            editTextFourth.setVisibility(View.GONE);
        });

    }


    public void init(){
        imageLogo = findViewById(R.id.imageLogo);
        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInButton);
        searchButton = findViewById(R.id.searchButton);
        editTextFirst = findViewById(R.id.editTextFirst);
        editTextSecond = findViewById(R.id.editTextSecond);
        editTextThird = findViewById(R.id.editTextThird);
        editTextFourth = findViewById(R.id.editTextFourth);
    }
}