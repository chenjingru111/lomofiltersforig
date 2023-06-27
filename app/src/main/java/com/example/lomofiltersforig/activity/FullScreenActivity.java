package com.example.lomofiltersforig.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lomofiltersforig.R;

import java.io.File;

public class FullScreenActivity extends AppCompatActivity {

    private ImageView iv_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        iv_full = (ImageView) findViewById(R.id.iv_full);
        findViewById(R.id.ll_all).setOnClickListener(v -> {
            finish();
        });
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("pic");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera/" + fileName);
        if (!file.exists()) {
            // 文件不存在，处理异常情况...
            Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Uri uri = Uri.fromFile(file);
            Glide.with(this)
                    .load(uri)
                    .into(iv_full);
        }
    }
}