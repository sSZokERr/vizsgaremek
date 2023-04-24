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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenProjectFragment extends Fragment {
    private List<PostImages> images = new ArrayList<>();
    private List<PostImages> projects = new ArrayList<>();
    private List<PostImages> actualProject = new ArrayList<>();
    private ListView listViewImagesFrag;
    private int projectId;
    private int picture;
    private ImageView imageFrag, projectCover;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_project, container, false);
        init(view);
        RequestTask task = new RequestTask("http://10.0.2.2:3000/getFiles", "GET");
        task.execute();

        return view;
    }

    public void init(View view){
        listViewImagesFrag = view.findViewById(R.id.listViewFrag);
        imageFrag = view.findViewById(R.id.imageViewTestFrag);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String project[] = sharedPreferences.getString("Dynamiclly loaded image", null).split(";");
        picture = Integer.parseInt(project[0]);
        projectId = Integer.parseInt(project[1]);
        imageFrag.setVisibility(View.GONE);
    }

    private void initListView(){
        ImageAdapter adapter = new ImageAdapter();
        listViewImagesFrag.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends ArrayAdapter<PostImages> {
        public ImageAdapter() {
            super(getActivity().getApplicationContext(), R.layout.project_adapter, actualProject);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.project_adapter, null, false);
            PostImages actualImage = actualProject.get(position);
            TextView textViewRecommended = view.findViewById(R.id.editTitle);
            textViewRecommended.setEnabled(false);
            ImageView imageViewRecommended = view.findViewById(R.id.newProjectButton);
                Picasso.get().load(actualImage.getUrl()).into(imageViewRecommended);


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
                Toast.makeText(getActivity().getApplicationContext(),
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
                Toast.makeText(getActivity().getApplicationContext(), "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                            PostImages[] imageArray = converter.fromJson(response.getContent(), PostImages[].class);
                            images.clear();
                            images.addAll(Arrays.asList(imageArray));
                            images.remove(0);
                            initListView();
                            Picasso.get().load(images.get(1).getUrl()).into(imageFrag);
                            for (int i = 0; i < images.size(); i++) {
                                if (images.get(i).getImageType() == 1){
                                    projects.add(images.get(i));
                                }
                            }
                        for (int i = 0; i < images.size(); i++) {
                            if (images.get(i).getProject() == projectId){
                                actualProject.add(images.get(i));
                            }
                        }

                            break;
                        }
                }

            }
        }
    }