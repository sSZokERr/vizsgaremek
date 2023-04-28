package com.example.vizsgaremek_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import android.graphics.Color;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private ImageView imageLogo;
    private Animation fadeIn;
    private AppCompatButton registerButton, logInButton, continueButton;
    private EditText editTextFirst, editTextSecond, editTextThird, editTextFourth, editTextFifth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        VideoView videoview = (VideoView) findViewById(R.id.videoViewBackground);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background);
        videoview.setVideoURI(uri);
        videoview.start();

        imageLogo.startAnimation(fadeIn);

        continueButton.setOnClickListener(v1 -> {
            LogIn();
        });

        registerButton.setOnClickListener(v -> {
            logInButton.setBackgroundColor(Color.TRANSPARENT);
            logInButton.setTextColor(Color.BLACK);
            registerButton.setBackgroundColor(Color.BLACK);
            registerButton.setTextColor(Color.WHITE);

            editTextFirst.setHint("Fist name");
            editTextSecond.setVisibility(View.VISIBLE);
            editTextSecond.setHint("Last name");
            editTextThird.setVisibility(View.VISIBLE);
            editTextThird.setHint("Email");
            editTextFourth.setVisibility(View.VISIBLE);
            editTextFourth.setHint("Password");
            editTextFifth.setVisibility(View.VISIBLE);
            editTextFifth.setHint("Password again");

            continueButton.setOnClickListener(v12 -> {
                Register();
            });
        });
        logInButton.setOnClickListener(v -> {
            logInBtn();

            continueButton.setOnClickListener(v1 -> {
                LogIn();
            });
        });

    }



    public void init() {
        imageLogo = findViewById(R.id.imageLogo);
        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInButton);
        editTextFirst = findViewById(R.id.editTextFirst);
        editTextSecond = findViewById(R.id.editTextSecond);
        editTextThird = findViewById(R.id.editTextThird);
        editTextFifth = findViewById(R.id.editTextFifth);
        editTextFourth = findViewById(R.id.editTextFourth);
        continueButton = findViewById(R.id.continueButton);
    }

    void Register() {
        String url = "http://10.0.2.2:3000/register";
        String convertedFirstName = editTextFirst.getText().toString().trim();
        String convertedLastName = editTextSecond.getText().toString().trim();
        String convertedEmail = editTextThird.getText().toString().trim();
        String convertedPassword = editTextFourth.getText().toString().trim();
        String convertedPasswordAgain = editTextFifth.getText().toString().trim();
        Gson gson = new Gson();
        NewUser user = new NewUser(convertedFirstName, convertedLastName, convertedEmail, convertedPassword, convertedPasswordAgain);
        RequestTask task = new RequestTask(url, "POST", gson.toJson(user));
        task.execute();
    }

    void LogIn() {
        String url = "http://10.0.2.2:3000/login";
        String email = editTextFirst.getText().toString().trim();
        String password = editTextSecond.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@")) {
            Toast.makeText(this, "Nem megfelelő az email", Toast.LENGTH_SHORT).show();
        } else if (password.toString().length() < 8) {
            Toast.makeText(this, "A jelszónak legalább 8 karakternek kell lennie", Toast.LENGTH_SHORT).show();
        }

        Login user = new Login(email, password);
        Gson jsonConverter = new Gson();
        RequestTask task2 = new RequestTask(url, "POST", jsonConverter.toJson(user));
        task2.execute();
        Toast.makeText(MainActivity.this, "Sikeres bejelentkezes", Toast.LENGTH_SHORT).show();
    }

    public void logInBtn() {
        registerButton.setBackgroundColor(Color.TRANSPARENT);
        registerButton.setTextColor(Color.BLACK);
        logInButton.setBackgroundColor(Color.BLACK);
        logInButton.setTextColor(Color.WHITE);


        editTextFirst.setHint("Email");
        editTextSecond.setHint("Password");
        editTextFirst.setText("");
        editTextSecond.setText("");
        editTextSecond.setVisibility(View.VISIBLE);
        editTextThird.setVisibility(View.GONE);
        editTextFourth.setVisibility(View.GONE);
        editTextFifth.setVisibility(View.GONE);
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }


        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestType) {
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(MainActivity.this, "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            } else {
                switch (requestType) {
                    case "GET":
                        break;
                    case "POST":
                        if (requestUrl.equals("http://10.0.2.2:3000/login")) {
                            Token token = converter.fromJson(
                                    response.getContent(), Token.class);
                            String tokenString = token.getToken();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", tokenString);
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Sikeres regisztracio", Toast.LENGTH_SHORT).show();
                            Log.d("Sikeres:", response.getContent());
                            logInBtn();
                        }
                        break;
                }
            }
        }
    }


}
