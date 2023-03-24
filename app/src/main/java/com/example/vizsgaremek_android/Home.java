package com.example.vizsgaremek_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private List<Image> imageList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        listView.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends ArrayAdapter<Image> {
        public ImageAdapter() {
            super(Home.this, R.layout.image_adapters, imageList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.image_adapters, null, false);
            Image actualImage = imageList.get(position);
            ImageView imageViewAdapter = view.findViewById(R.id.imageViewAdapter);

            Picasso.get().load(actualImage.getUrl()).into(imageViewAdapter);
            return view;
        }
    }
    void init(){
        listView = findViewById(R.id.listView);
    }
}