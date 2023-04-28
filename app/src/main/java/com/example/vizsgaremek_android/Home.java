package com.example.vizsgaremek_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {
    // Private fields
    private List<PostImages> images = new ArrayList<>();
    private List<PostImages> projects = new ArrayList<>();
    private ListView listViewImages;
    private ImageView image;
    private String defaultProf;
    private ImageView imageViewRecommended;
    private String url = "http://10.0.2.2:3000/getFiles";
    public List<GetUserDetails> users = new ArrayList<>();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FrameLayout frameLayout;

    private List<GetProjectsData> projectData = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    // On create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        init();

        // Set up the navigation drawer and its listener
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    // Show the list of images
                    frameLayout.setVisibility(View.GONE);
                    listViewImages.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                    break;
                case R.id.account:
                    // Show the user's profile
                    image.setVisibility(View.GONE);
                    listViewImages.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, new ProfileFragment()).commit();
                    break;
                case R.id.logout:
                    // Remove the saved token, start the login activity, and finish the current activity
                    SharedPreferences preferences = getSharedPreferences("MyData", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("token").apply();
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.newProject:
                    // Show the screen for creating a new project
                    image.setVisibility(View.GONE);
                    listViewImages.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, new NewProject()).commit();
                    break;
                case R.id.search:
                    // Show the screen for searching
                    image.setVisibility(View.GONE);
                    listViewImages.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, new SearchFragment()).commit();
                    break;
            }
            // Close the drawer and return true to indicate that the item was handled
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        RequestTask task = new RequestTask(url, "GET");
        RequestTask task2 = new RequestTask("http://10.0.2.2:3000/users", "GET");
        RequestTask task3 = new RequestTask("http://10.0.2.2:3000/getProjects", "GET");
        task.execute();
        task2.execute();
        task3.execute();
    }

    void init() {
        listViewImages = findViewById(R.id.listView);
        listViewImages.setAdapter(new ImageAdapter());
        imageViewRecommended = findViewById(R.id.imageViewAdapter);
        image = findViewById(R.id.imageViewTest);
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        frameLayout = findViewById(R.id.fragmentContainer);

    }

    private class ImageAdapter extends ArrayAdapter<PostImages> {
        public ImageAdapter() {
            super(Home.this, R.layout.image_adapters, projects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.image_adapters, null, false);
            if (projects.get(position).getUserid() != 0) {
                PostImages actualImage = projects.get(position);
                ImageView imageViewRecommended = view.findViewById(R.id.imageViewAdapter);
                TextView textViewRecommended = view.findViewById(R.id.artTitle);
                TextView artist = view.findViewById(R.id.textViewAdapter);
                for (int i = 0; i < titles.size(); i++) {
                    Log.e("For loop start", ""+projectData.get(i).getUserid());
                    Log.e("For loop", ""+actualImage.getUserid());
                    Log.e("For loop", ""+actualImage.getProject());
                    Log.e("For loop end", ""+projectData.get(i).getProjectId());

                    if (actualImage.getUserid() == projectData.get(i).getUserid() && actualImage.getProject() == projectData.get(i).getProjectId()){
                         textViewRecommended.setText(projectData.get(i).getProjectTitle());
                    }
                }



                Picasso.get().load(actualImage.getUrl()).into(imageViewRecommended);
                imageViewRecommended.setId(position);
                int project = actualImage.getProject();
                imageViewRecommended.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // handle click event here
                        // you can get the position of the clicked image using v.getId()
                        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Dynamiclly loaded image", ""+v.getId()+";"+project);
                        Log.e("asdasd", ""+v.getId() );
                        editor.commit();
                        image.setVisibility(View.GONE);
                        listViewImages.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new OpenProjectFragment()).commit();
                    }
                });
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == projects.get(position).getUserid()){
                        artist.setText(
                                users.get(i).getFirstName()
                                + " "
                                + users.get(i).getLastName());
                    }
                }
            }

            return view;
        }
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
                Toast.makeText(Home.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            ArrayList<String> urls = new ArrayList<String>();
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(Home.this, "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        if (requestUrl == "http://10.0.2.2:3000/getProjects") {
                            GetProjectsData[] projectArray = converter.fromJson(response.getContent(), GetProjectsData[].class);
                            projectData.clear();
                            projectData.addAll(Arrays.asList(projectArray));
                            for (int i = 0; i < projectData.size(); i++) {
                                if (projectData.get(i).getProjectTitle() == ""){
                                    titles.add("Default");
                                }else {
                                    titles.add(projectData.get(i).getProjectTitle());
                                    Log.e("IDD","" + projectData.get(i).getProjectId());
                                }

                            }
                        } else if (requestUrl.equals("http://10.0.2.2:3000/users")) {
                            GetUserDetails[] userArray = converter.fromJson(response.getContent(), GetUserDetails[].class);
                            users.clear();
                            users.addAll(Arrays.asList(userArray));
                        } else {
                            PostImages[] imageArray = converter.fromJson(response.getContent(), PostImages[].class);
                            images.clear();
                            images.addAll(Arrays.asList(imageArray));
                            defaultProf = images.get(0).getUrl();
                            images.remove(0);
                            Picasso.get().load(images.get(1).getUrl()).into(image);
                            for (int i = 0; i < images.size(); i++) {
                                if (images.get(i).getImageType() == 1){
                                    projects.add(images.get(i));
                                }
                            }
                            break;
                        }
                }

            }
        }
    }
}
