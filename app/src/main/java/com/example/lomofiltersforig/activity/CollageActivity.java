package com.example.lomofiltersforig.activity;

import static com.otaliastudios.cameraview.CameraView.PERMISSION_REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atech.glcamera.BrightnessController;
import com.bumptech.glide.Glide;
import com.example.lomofiltersforig.R;
import com.example.lomofiltersforig.fragment.AdjustFragment;
import com.example.lomofiltersforig.fragment.BackgroundFragment;
import com.example.lomofiltersforig.fragment.SpaceFragment;
import com.example.lomofiltersforig.tools.GlideEngine;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.PuzzleCallback;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

public class CollageActivity extends AppCompatActivity {

    private ImageView iv_adjust;
    private ImageView iv_layout;
    private ImageView iv_background;
    private ImageView iv_space;
    private ImageView iv_ratio;
    private FrameLayout fragment_container;
    private ImageView iv_picture;
    private TextView iv_load;
    private SeekBar mSeekBar;
    private BrightnessController mBrightnessController;
    private RelativeLayout rl_changebackground;
    private ArrayList<Photo> selectedPhotoList = new ArrayList<>();
    private ArrayList<String> selectedPhotoPathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        initView();
    }

    private void initView() {

        iv_adjust = (ImageView) findViewById(R.id.iv_adjust);
        iv_layout = (ImageView) findViewById(R.id.iv_layout);
        iv_background = (ImageView) findViewById(R.id.iv_background);
        iv_space = (ImageView) findViewById(R.id.iv_space);
        iv_ratio = (ImageView) findViewById(R.id.iv_ratio);
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_load = (TextView) findViewById(R.id.iv_load);
        mSeekBar = findViewById(com.atech.glcamera.R.id.brightness_seekbar);
        mBrightnessController = new BrightnessController(this);
        rl_changebackground = (RelativeLayout) findViewById(R.id.rl_changebackground);

        mSeekBar.setVisibility(View.INVISIBLE);
// 检查并请求写入系统设置的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }

        // 获取当前屏幕亮度值，并设置给进度条和文本框
        int currentBrightness = mBrightnessController.getBrightness();
        mSeekBar.setProgress(currentBrightness);
        //mValueTextView.setText(getString(R.string.brightness_value, currentBrightness));

        // 添加进度条变化监听器
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrightnessController.setBrightness(progress); // 设置屏幕亮度
                //mValueTextView.setText(getString(R.string.brightness_value, progress)); // 更新亮度文本框
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        findViewById(R.id.back).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.iv_load).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        findViewById(R.id.ll_adjust).setOnClickListener(v -> {
            changeColor(R.drawable.adjust,R.drawable.unlayout,R.drawable.unbackground,R.drawable.unspace,R.drawable.unratio);
            FragmentManager fragmentManager  = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AdjustFragment adjustFragment = new AdjustFragment();
            fragmentTransaction.replace(R.id.fragment_container,adjustFragment,"AdjustFragment_TAG");
            fragmentTransaction.commit();
        });
        findViewById(R.id.ll_layout).setOnClickListener(v -> {
            changeColor(R.drawable.unadjust,R.drawable.layout,R.drawable.unbackground,R.drawable.unspace,R.drawable.unratio);
            //TODO
            EasyPhotos.createAlbum(CollageActivity.this,false, GlideEngine.getInstance())
                    .setCount(9)
                    .setPuzzleMenu(false)
                    .start(new SelectCallback() {
                        @Override
                        public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                            EasyPhotos.startPuzzleWithPhotos(CollageActivity.this, photos, false,GlideEngine.getInstance(), new PuzzleCallback() {
                                @Override
                                public void onResult(Photo photo, String path) {
                                    if (photo!=null){
                                        Toast.makeText(CollageActivity.this, "Puzzle saved successfully", Toast.LENGTH_SHORT).show();
                                        Glide.with(CollageActivity.this)
                                                .load(path)
                                                .into(iv_picture);
                                    }
                                }
                            });
                        }
                    });
        });
        findViewById(R.id.ll_background).setOnClickListener(v -> {
            changeColor(R.drawable.unadjust,R.drawable.unlayout,R.drawable.background,R.drawable.unspace,R.drawable.unratio);
            FragmentManager fragmentManager  = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            BackgroundFragment backgroundFragment = new BackgroundFragment();
            fragmentTransaction.replace(R.id.fragment_container,backgroundFragment,"BackgroundFragment_TAG");
            fragmentTransaction.commit();
        });
        findViewById(R.id.ll_space).setOnClickListener(v -> {
            changeColor(R.drawable.unadjust,R.drawable.unlayout,R.drawable.unbackground,R.drawable.space,R.drawable.unratio);
            FragmentManager fragmentManager  = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SpaceFragment spaceFragment = new SpaceFragment();
            fragmentTransaction.replace(R.id.fragment_container,spaceFragment,"SpaceFragment_TAG");
            fragmentTransaction.commit();
        });
        findViewById(R.id.ll_ratio).setOnClickListener(v -> {
            changeColor(R.drawable.unadjust,R.drawable.unlayout,R.drawable.unbackground,R.drawable.unspace,R.drawable.ratio);
            EasyPhotos.createAlbum(this,true,GlideEngine.getInstance())
                    .isCrop(true)
                    .setFreeStyleCropEnabled(true)
                    .enableSingleCheckedBack(true)
                    .start(callback);
        });



    }

    private void changeColor(int a1,int a2,int a3,int a4,int a5){
        iv_adjust.setImageResource(a1);
        iv_layout.setImageResource(a2);
        iv_background.setImageResource(a3);
        iv_space.setImageResource(a4);
        iv_ratio.setImageResource(a5);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                // 获取选择的图片
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                // 将图片显示在ImageView上
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                iv_picture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Drawable drawable = iv_picture.getDrawable();
        if (drawable!=null){
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap
            iv_load.setText("Save");
            iv_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_load.setText("Save");
                   /* Drawable drawable = iv_picture.getDrawable(); // 获取 ImageView 中的 Drawable
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap
                    AdjustFragment.saveBitmapToGallery(CollageActivity.this,bitmap);*/
                }
            });
        }
    }

    private SelectCallback callback = new SelectCallback() {
        @Override
        public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
            selectedPhotoList.clear();
            selectedPhotoList.addAll(photos);
            selectedPhotoPathList.clear();
            selectedPhotoPathList.addAll(paths);
            /*adapter.notifyDataSetChanged();
            rvImage.smoothScrollToPosition(0);*/
            Glide.with(CollageActivity.this)
                    .load(paths)
                    .into(iv_picture);
        }
    };
}