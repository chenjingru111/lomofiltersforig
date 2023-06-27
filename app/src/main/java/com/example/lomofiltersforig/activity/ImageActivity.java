package com.example.lomofiltersforig.activity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lomofiltersforig.R;

import java.io.File;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView deleteButton;
    private String imagePath;
    private ImageView iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);
        deleteButton = findViewById(R.id.deleteButton);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        imagePath = getIntent().getStringExtra("imagePath");
        Glide.with(this)
                .load(imagePath)
                .centerCrop()
                .into(imageView);

        iv_delete.setOnClickListener(view -> {
            deleteImage();
            setResult(RESULT_OK);
            Toast.makeText(this, "picture has already deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void deleteImage() {
        File file = new File(imagePath);
        if (file.exists()) {
            file.delete();
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "=?", new String[]{imagePath});
        }
    }
}