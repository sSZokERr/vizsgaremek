package com.example.vizsgaremek_android;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileFragment extends Fragment {

    private ImageView profButton, imageViewTestFrag;
    private EditText studiesText, occupationText, workText, aboutMeText;
    private TextView artistNameText;
    public List<GetUserDetails> users = new ArrayList<>();
    private List<PostImages> images = new ArrayList<>();
    private List<UpdateProfileDetails> update = new ArrayList<>();
    public List<GetUserDetails> loggedUser = new ArrayList<>();
    private static final int PICK_IMAGE_REQ = 1;
    private Button edit;
    private Uri imageUri;
    private Boolean buttonB = false;
    private List<PostImages> currentUserProjects = new ArrayList<>();
    private ListView listViewImagesFrag;
    public int UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        studiesText.setEnabled(false);
        occupationText.setEnabled(false);
        workText.setEnabled(false);
        aboutMeText.setEnabled(false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "token");
        Log.e("token", token);
        JWT jwt = new JWT(token);
        String claim = jwt.getClaim("id").asString();
        UserId = Integer.parseInt(claim);
        Log.e("userid", "" + UserId);

        RequestTask task1 = new RequestTask("http://10.0.2.2:3000/users", "GET");
        task1.execute();
        RequestTask task2 = new RequestTask("http://10.0.2.2:3000/getFiles", "GET");
        task2.execute();
        edit.setOnClickListener(view12 -> {
            if (!buttonB) {
                studiesText.setEnabled(true);
                occupationText.setEnabled(true);
                workText.setEnabled(true);
                aboutMeText.setEnabled(true);
                edit.setText("Save");
                buttonB = true;
            } else {
                SaveButton();
            }
        });

        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        return view;
    }

    public void init(View view) {
        profButton = view.findViewById(R.id.profButton);
        studiesText = view.findViewById(R.id.studiesText);
        occupationText = view.findViewById(R.id.occupationText);
        workText = view.findViewById(R.id.workText);
        aboutMeText = view.findViewById(R.id.aboutMeText);
        artistNameText = view.findViewById(R.id.artistNameText);
        edit = view.findViewById(R.id.editProfButton);
        listViewImagesFrag = view.findViewById(R.id.listViewFrag);
        imageViewTestFrag = view.findViewById(R.id.imageViewTestFrag);
        listViewImagesFrag.setAdapter(new ImageAdapter());
    }

    private void initListView(){
        ImageAdapter adapter = new ImageAdapter();
        listViewImagesFrag.setAdapter(new ImageAdapter());
    }

    private void Update() {
        String upStudies = studiesText.getText().toString().trim();
        String upOccupation = occupationText.getText().toString().trim();
        String upWork = workText.getText().toString().trim();
        String upAboutMe = aboutMeText.getText().toString().trim();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "token");
        Log.e("token", token);
        JWT jwt = new JWT(token);
        String claim = jwt.getClaim("id").asString();
        int id = Integer.parseInt(claim);
        UpdateProfileDetails updateProf = new UpdateProfileDetails(id, upStudies, upOccupation, upWork, upAboutMe);
        Gson gson = new Gson();
        RequestTask task3 = new RequestTask("http://10.0.2.2:3000/updateProfileDetails", "POST", gson.toJson(updateProf));
        task3.execute();
    }

    private void SaveButton() {
        studiesText.setEnabled(false);
        occupationText.setEnabled(false);
        workText.setEnabled(false);
        aboutMeText.setEnabled(false);
        edit.setText("Edit profile");
        Update();
        buttonB = false;
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
            UplaodProfilePicture(imageUri);

        }
    }

    private void UplaodProfilePicture(Uri uri){
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

            // Create a ProfilePictureUpload object with the user ID and base64 string
            PictureUpload upload = new PictureUpload(image, UserId, 0, 0,-1);

            // Include the base64 string in the POST request
            Gson gson = new Gson();
            RequestTask task5 = new RequestTask("http://10.0.2.2:3000/uploadAndroid", "POST", gson.toJson(upload));
            task5.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getFileExt(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                                    Picasso.get().load(images.get(i).getUrl()).into(profButton);
                                    break;
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
                        if (requestUrl == "http://10.0.2.2:3000/uploadAndroid"){
                            Toast.makeText(getActivity(), "Succesfull", Toast.LENGTH_SHORT).show();
                        }else {
                            update.clear();
                        }

                }
            }
        }
    }
}