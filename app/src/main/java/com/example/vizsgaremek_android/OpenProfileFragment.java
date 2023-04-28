package com.example.vizsgaremek_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenProfileFragment extends Fragment {

    private TextView studiesText, occupationText, workText, aboutMeText, artistNameText;
    private ListView listViewImagesFrag;
    private ImageView profilePic, imageViewTestFrag;

    public List<GetUserDetails> users = new ArrayList<>();
    private List<PostImages> images = new ArrayList<>();
    private List<PostImages> currentUserProjects = new ArrayList<>();
    public List<GetUserDetails> loggedUser = new ArrayList<>();

    public int UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_profile, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("Profile", null);
        UserId = Integer.parseInt(id);
        init(view);
        RequestTask task1 = new RequestTask("http://10.0.2.2:3000/users", "GET");
        task1.execute();
        RequestTask task2 = new RequestTask("http://10.0.2.2:3000/getFiles", "GET");
        task2.execute();

        return view;
    }

    private void init(View view) {
        studiesText = view.findViewById(R.id.studiesTextOpen);
        artistNameText = view.findViewById(R.id.artistNameTextOpen);
        occupationText = view.findViewById(R.id.occupationTextOpen);
        workText = view.findViewById(R.id.workTextOpen);
        aboutMeText = view.findViewById(R.id.aboutMeTextOpen);
        listViewImagesFrag = view.findViewById(R.id.listViewOpen);
        profilePic = view.findViewById(R.id.profilePicOpen);
        imageViewTestFrag = view.findViewById(R.id.imageViewTestOpen);
        imageViewTestFrag.setVisibility(View.GONE);
    }

    private void initListView(){
        ImageAdapter adapter = new ImageAdapter();
        listViewImagesFrag.setAdapter(new ImageAdapter());
    }
    private class ImageAdapter extends ArrayAdapter<PostImages> {
        public ImageAdapter() {
            super(getActivity().getApplicationContext(), R.layout.image_adapters, currentUserProjects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.image_adapters, null, false);
            PostImages actualImage = currentUserProjects.get(position);
            ImageView imageViewRecommended = view.findViewById(R.id.imageViewAdapter);
            Picasso.get().load(actualImage.getUrl()).into(imageViewRecommended);
            Log.e("Picasso", "" + actualImage.getImageType());
            return view;
        }
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;
        File requestParam;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }
        public RequestTask(String requestUrl, String requestType, File requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParam = requestParams;
        }

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            Gson converter = new Gson();
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
                Toast.makeText(getActivity().getApplicationContext(),
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(getActivity().getApplicationContext(), "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        if (requestUrl.equals("http://10.0.2.2:3000/users")) {
                            GetUserDetails[] userArray = converter.fromJson(response.getContent(), GetUserDetails[].class);
                            users.clear();
                            users.addAll(Arrays.asList(userArray));
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getId() == UserId) {
                                    loggedUser.addAll(Arrays.asList(users.get(i)));
                                    studiesText.setText(loggedUser.get(0).getStudies());
                                    occupationText.setText(loggedUser.get(0).getOccupation());
                                    workText.setText(loggedUser.get(0).getWorkExperience());
                                    aboutMeText.setText(loggedUser.get(0).getAboutMe());
                                    Log.e("asda", loggedUser.get(0).getLastName());
                                    artistNameText.setText(loggedUser.get(0).getFirstName() + " " + loggedUser.get(0).getLastName());
                                }
                            }
                        } else if (requestUrl.equals("http://10.0.2.2:3000/getFiles")) {
                            PostImages[] imageArray = converter.fromJson(response.getContent(), PostImages[].class);
                            images.clear();
                            currentUserProjects.clear();
                            images.addAll(Arrays.asList(imageArray));
                            images.remove(0);
                            initListView();
                            for (int i = 0; i < images.size(); i++) {
                                if (loggedUser.get(0).getId() == images.get(i).getUserid() && images.get(i).getImageType() == 0) {
                                    Picasso.get().load(images.get(i).getUrl()).into(profilePic);
                                }
                            }
                            for (int i = 0; i < images.size(); i++) {
                                if (images.get(i).getUserid() == UserId) {
                                    currentUserProjects.add(images.get(i));
                                    Log.e("userid", "" + UserId);
                                    Log.e("userid", "" + images.get(i).getUserid());
                                    Log.e("i", "" + i);
                                    Log.e("currentprojectsize", "" + currentUserProjects.size());
                                }
                            }
                            Picasso.get().load(currentUserProjects.get(0).getUrl()).into(imageViewTestFrag);
                        }
                        break;
                    case "POST":
                }
            }
        }
    }
}

