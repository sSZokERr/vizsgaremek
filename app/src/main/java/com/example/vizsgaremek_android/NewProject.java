package com.example.vizsgaremek_android;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class NewProject extends Fragment {
    private ImageView projectCover;
    private TextView projectTitle;
    private Button submit;
    private int userId;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    private List<String> images;
    private ListView listViewFrag;
    private List<PostImages> newProject = new ArrayList<>();
    private static final int PICK_IMAGE_REQ = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_project, container, false);
        init(view);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Upload();

            }
        });
        projectCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newProject.size() == 0) {
                    titleList.add("Title");
                    descList.add("Description");
                    Add();
                    openFileChooser();
                }
            }
        });
        return view;
    }

    public void init(View view) {
        projectCover = view.findViewById(R.id.projectCover);
        projectTitle = view.findViewById(R.id.projectTitle);
        submit = view.findViewById(R.id.submitButton);
        listViewFrag = view.findViewById(R.id.listViewFrag);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.e("token", token);
        JWT jwt = new JWT(token);
        String claim = jwt.getClaim("id").asString();
        userId = Integer.parseInt(claim);
    }
    private void initListView(){
        ImageAdapter adapter = new ImageAdapter();
        listViewFrag.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends ArrayAdapter<PostImages> {
        public ImageAdapter() {
            super(getActivity().getApplicationContext(), R.layout.project_adapter, newProject);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.project_adapter, null, false);
            PostImages actualImage = newProject.get(position);
            ImageView imageViewRecommended = view.findViewById(R.id.newProjectButton);
            EditText editTitleRec = view.findViewById(R.id.editTitle);
            EditText editDescRec = view.findViewById(R.id.editDesc);
            Picasso.get().load(actualImage.getUrl()).into(imageViewRecommended);
            editTitleRec.setText(titleList.get(position));
            editDescRec.setText(descList.get(position));
            editTitleRec.setId(position + 100);
            editDescRec.setId(position + 200);
            editTitleRec.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    titleList.set(position, charSequence.toString().trim());
                    Log.e("testerMothafaka", titleList.get(position));
                }

                @Override
                public void afterTextChanged(Editable editable) {}

            });
            editDescRec.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    descList.set(position, charSequence.toString().trim());
                    Log.e("testerMothafaka", descList.get(position));
                }

                @Override
                public void afterTextChanged(Editable editable) {}

            });
            imageViewRecommended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    descList.add("Description");
                    titleList.add("Title");
                    Add();
                }
            });
            return view;
        }
    }

    private void Add(){
        PostImages newAdd = new PostImages(0,"newproject.png",0,0);
        newProject.add(newAdd);
        initListView();
    }

    private void openFileChooser() {
        //Ezt kell meghivni gombra tovabbmegy az onactivityresultra
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            ConvertToString(imageUri);

        }
    }
    private void ConvertToString(Uri uri){
        try {
            // Load the image file
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            byte[] dataBytes = new byte[inputStream.available()];
            try {
                inputStream.read(dataBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputStream.close();

            // Encode the image data to a base64 string
            String image = null;
            try {
                image = Base64.encodeToString(dataBytes, Base64.DEFAULT);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            images.add(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Upload(){
        String title = projectTitle.getText().toString().trim();
        JSONObject desc = new JSONObject();
        JSONObject titles = new JSONObject();
        JSONArray arrayDesc = new JSONArray();
        JSONArray arrayTitle = new JSONArray();
        for (int i = 0; i < descList.size(); i++) {
            arrayDesc.put(descList.get(i));
            arrayTitle.put(titleList.get(i));
        }
        try {
            desc.put("description", arrayDesc);
            titles.put("titles", arrayTitle);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JSONArray data = new JSONArray();
        data.put(desc);
        data.put(titles);
        Gson gson = new Gson();
        ProjectDataUpload upload = new ProjectDataUpload(userId, title, data.toString());
        RequestTask newProject = new RequestTask("http://10.0.2.2:3000/newProject", gson.toJson(upload));
        newProject.execute();
        PictureUpload coverUpload = new PictureUpload(images.get(0), userId, 1, 0,0);
        RequestTask coverUp = new RequestTask("http://10.0.2.2:3000/uploadAndroid", "POST", gson.toJson(upload));
        coverUp.execute();
        for (int i = 1; i < images.size(); i++) {
            PictureUpload pictureUpload = new PictureUpload(images.get(i), userId, 0, 0,-1);
            RequestTask picUpload = new RequestTask("http://10.0.2.2:3000/uploadAndroid", "POST", gson.toJson(upload));
            picUpload.execute();
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
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            } else {
                switch (requestType) {
                    case "GET":
                        break;
                    case "POST":
                        break;
                }
            }
        }
    }
}