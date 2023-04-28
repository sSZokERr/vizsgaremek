package com.example.vizsgaremek_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;


public class SearchFragment extends Fragment {

    private ImageAdapter adapter;
    private EditText search;
    public List<GetUserDetails> users = new ArrayList<>();
    public List<GetUserDetails> usersOnce = new ArrayList<>();
    public int UserId;
    private ListView listViewImagesFrag;
    private List<PostImages> images = new ArrayList<>();
    private List<PostImages> searchResult = new ArrayList<>();

    private ImageView imageViewTestFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);
        initListView();
        RequestTask task = new RequestTask("http://10.0.2.2:3000/users", "GET");
        task.execute();
        RequestTask task2 = new RequestTask("http://10.0.2.2:3000/getFiles", "GET");
        task2.execute();
        adapter = new ImageAdapter();
        listViewImagesFrag.setAdapter(adapter);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = search.getText().toString().trim();
                for (int i = 0; i < users.size(); i++) {
                    if (usersOnce.get(i).getFirstName().toLowerCase(Locale.ROOT).contains(text.toLowerCase()) || usersOnce.get(i).getLastName().toLowerCase(Locale.ROOT).contains(text.toLowerCase())){
                        int id = usersOnce.get(i).getId();
                        for (int j = 0; j < images.size(); j++) {
                            if (id == images.get(j).getUserid() && images.get(j).getImageType() == 0){
                                searchResult.add(images.get(j));
                                break;
                            }
                        }
                    }
                }
                initListView();
            }
        });
        return view;
    }

    public void init(View view){
        search = view.findViewById(R.id.searchView);
        imageViewTestFrag = view.findViewById(R.id.imageViewTestFrag);
        listViewImagesFrag = view.findViewById(R.id.listViewFrag);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "token");
        Log.e("token", token);
        JWT jwt = new JWT(token);
        String claim = jwt.getClaim("id").asString();
        UserId = Integer.parseInt(claim);
    }
    private void initListView(){
        listViewImagesFrag.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends ArrayAdapter<PostImages> {
        public ImageAdapter() {
            super(getActivity().getApplicationContext(), R.layout.search_adapters, searchResult);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.image_adapters, null, false);
            PostImages actualImage = searchResult.get(position);
            ImageView imageViewRecommended = view.findViewById(R.id.imageViewAdapter);
            TextView textViewRecommended = view.findViewById(R.id.textViewAdapter);
            int id = actualImage.getUserid();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == id){
                    textViewRecommended.setText(users.get(i).getFirstName() + " " + users.get(i).getLastName());
                }
            }
            imageViewRecommended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Profile", ""+actualImage.getUserid());
                    editor.commit();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new OpenProfileFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });
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
                        if (requestUrl.equals("http://10.0.2.2:3000/users")) {
                            GetUserDetails[] userArray = converter.fromJson(response.getContent(), GetUserDetails[].class);
                            users.clear();
                            users.addAll(Arrays.asList(userArray));
                            int ids[] = new int[users.size()];
                            for (int i = 0; i < users.size(); i++) {
                                if (!Arrays.asList(ids).contains(users.get(i).getId())){
                                    usersOnce.addAll(Arrays.asList(users.get(i)));
                                    ids[i] = i;
                                }
                            }

                            }else {
                                PostImages[] imageArray = converter.fromJson(response.getContent(), PostImages[].class);
                                images.clear();
                                images.addAll(Arrays.asList(imageArray));
                                images.remove(0);
                                initListView();
                            }
                            }
                }
            }
        }
    }
