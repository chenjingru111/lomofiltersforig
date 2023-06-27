package com.example.lomofiltersforig.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lomofiltersforig.R;

import java.io.File;

public class HalffullScreenActivity extends AppCompatActivity {

    private ImageView iv_half_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halffull_screen);

        iv_half_full = (ImageView) findViewById(R.id.iv_half_full);

        findViewById(R.id.ll_half_full).setOnClickListener(v -> {
            finish();
        });
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("half_pic");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera/" + fileName);
        if (!file.exists()) {
            // 文件不存在，处理异常情况...
            Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Uri uri = Uri.fromFile(file);
            Glide.with(this)
                    .load(uri)
                    .into(iv_half_full);
        }
    }
}