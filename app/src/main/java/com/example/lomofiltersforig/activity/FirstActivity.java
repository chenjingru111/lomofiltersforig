package com.example.lomofiltersforig.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.atech.glcamera.CameraActivity;
import com.example.lomofiltersforig.R;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_savedarea).setOnClickListener(v -> {
            startActivity(new Intent(FirstActivity.this, ShowPhotoActivity.class));
        });
        findViewById(R.id.ll_camera).setOnClickListener(v -> {
            startActivity(new Intent(FirstActivity.this, CameraActivity.class));
        });
        findViewById(R.id.ll_collage).setOnClickListener(v -> {
            startActivity(new Intent(FirstActivity.this, CollageActivity.class));
        });
    }
}