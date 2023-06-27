package com.atech.glcamera;

import static com.atech.glcamera.render.ByteFlowRender.IMAGE_FORMAT_I420;
import static com.atech.glcamera.render.ByteFlowRender.IMAGE_FORMAT_RGBA;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atech.glcamera.camera.Camera2FrameCallback;
import com.atech.glcamera.camera.Camera2Wrapper;
import com.atech.glcamera.camera.CameraUtil;
import com.atech.glcamera.frame.ByteFlowFrame;
import com.atech.glcamera.render.GLByteFlowRender;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity implements Camera2FrameCallback, GLByteFlowRender.Callback{
    private static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    GLSurfaceView mGLSurfaceView;
    Camera2Wrapper mCamera2Wrapper;
    GLByteFlowRender mByteFlowRender;
    Size mRootViewSize, mScreenSize;
    RelativeLayout mRootView;

    private static final String TAG = "MainActivity";

    RelativeLayout flCamera;

    private boolean mReadPixelsReady = true;
    private int mSampleSelectedIndex = 0;

    protected static final int LUT_A_SHADER_INDEX = 19;
    protected static final int LUT_B_SHADER_INDEX = 20;
    protected static final int LUT_C_SHADER_INDEX = 21;
    protected static final int LUT_D_SHADER_INDEX = 22;
    protected static final int ASCII_SHADER_INDEX = 29;

    private static final String[] SAMPLE_TITLES = {
            "shader0",
            "shader1",
            "shader2",
            "shader3",
            "shader4",
            "shader5",
            "shader6",
            "shader7",
            "shader8",
            "shader9",
            "shader10",
            "shader11",
            "shader12",
            "shader13",
            "shader14",
            "shader15",
            "shader16",
            "shader17",
            "shader18",
            "shader19",
            "shader20",
            "shader21",
            "shader22",
            "shader23",
            "shader24",
            "shader25",
            "shader26",
            "shader27",
            "shader28",
            "shader29",
            "shader30",
            "shader31",
            "shader32",
            "shader33"
    };


    private RecyclerView rl_choose;
    private ImageView img_camera;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SeekBar mSeekBar;
    private BrightnessController mBrightnessController;

    int light=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        img_camera = (ImageView) findViewById(R.id.img_camera);
        rl_choose = (RecyclerView) findViewById(R.id.rl_choose);
        final MyRecyclerViewAdapter myPreviewSizeViewAdapter = new MyRecyclerViewAdapter(this, Arrays.asList(SAMPLE_TITLES));
        myPreviewSizeViewAdapter.setSelectIndex(mSampleSelectedIndex);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rl_choose.setLayoutManager(manager);
        rl_choose.setAdapter(myPreviewSizeViewAdapter);
        rl_choose.scrollToPosition(mSampleSelectedIndex);


        findViewById(R.id.iv_back).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.iv_lookphoto).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName("com.example.lomofiltersforig", "com.example.lomofiltersforig.activity.ShowPhotoActivity");
            startActivity(intent);
        });
        mRootView = findViewById(R.id.root_view);
        mGLSurfaceView = new GLSurfaceView(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRootView.addView(mGLSurfaceView, p);
        mByteFlowRender = new GLByteFlowRender();
        mByteFlowRender.init(mGLSurfaceView);
        mByteFlowRender.loadShaderFromAssetsFile(mSampleSelectedIndex, getResources());

        mBrightnessController = new BrightnessController(this);
        mSeekBar = findViewById(R.id.brightness_seekbar);

        if (light==0){
            mSeekBar.setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.iv_light).setOnClickListener(v -> {
            if (light==0){
                light=1;
                mSeekBar.setVisibility(View.VISIBLE);
            }else {
                light=0;
                mSeekBar.setVisibility(View.INVISIBLE);
            }
        });
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

        myPreviewSizeViewAdapter.addOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                int selectIndex = myPreviewSizeViewAdapter.getSelectIndex();
                myPreviewSizeViewAdapter.setSelectIndex(position);
                myPreviewSizeViewAdapter.notifyItemChanged(selectIndex);
                myPreviewSizeViewAdapter.notifyItemChanged(position);
                mSampleSelectedIndex = position;

                switch (mSampleSelectedIndex) {
                    case LUT_A_SHADER_INDEX:
                        loadRGBAImage(R.drawable.lut_a, 0);
                        break;
                    case LUT_B_SHADER_INDEX:
                        loadRGBAImage(R.drawable.lut_b, 0);
                        break;
                    case LUT_C_SHADER_INDEX:
                        loadRGBAImage(R.drawable.lut_c, 0);
                        break;
                    case LUT_D_SHADER_INDEX:
                        loadRGBAImage(R.drawable.lut_d, 0);
                        break;
                    case ASCII_SHADER_INDEX:
                        loadRGBAImage(R.drawable.ascii_mapping, ASCII_SHADER_INDEX);
                        break;
                    default:
                }

                if (LUT_A_SHADER_INDEX <= mSampleSelectedIndex && mSampleSelectedIndex <= LUT_D_SHADER_INDEX) {
                    mByteFlowRender.loadShaderFromAssetsFile(LUT_A_SHADER_INDEX, getResources());
                } else {
                    mByteFlowRender.loadShaderFromAssetsFile(mSampleSelectedIndex, getResources());
                }

            }
        });
        //注意先执行render后初始化相机
        mCamera2Wrapper = new Camera2Wrapper(this);
        flCamera = findViewById(R.id.fl_camera);
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                if (mCamera2Wrapper != null) {
                    mCamera2Wrapper.capture();



                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            mCamera2Wrapper.startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, CAMERA_PERMISSION_REQUEST_CODE);
        }
        updateTransformMatrix(mCamera2Wrapper.getCameraId());
        if (mRootView != null) {
            updateGLSurfaceViewSize(mCamera2Wrapper.getPreviewSize());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            mCamera2Wrapper.stopCamera();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mByteFlowRender.unInit();

    }

    private void updateTransformMatrix(String cameraId) {
        if (Integer.valueOf(cameraId) == CameraCharacteristics.LENS_FACING_FRONT) {
            mByteFlowRender.setTransformMatrix(270, 0);
        } else {
            mByteFlowRender.setTransformMatrix(270, 1);
        }

    }

    public void updateGLSurfaceViewSize(Size previewSize) {
        Size fitSize = null;
        fitSize = CameraUtil.getFitInScreenSize(previewSize.getWidth(), previewSize.getHeight(), getScreenSize().getWidth(), getScreenSize().getHeight());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGLSurfaceView
                .getLayoutParams();
        params.width = fitSize.getWidth();
        params.height = fitSize.getHeight();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);

        mGLSurfaceView.setLayoutParams(params);
    }

    public Size getScreenSize() {
        if (mScreenSize == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            mScreenSize = new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        return mScreenSize;
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (hasPermissionsGranted(REQUEST_PERMISSIONS)) {
                mCamera2Wrapper.startCamera();
                updateTransformMatrix(mCamera2Wrapper.getCameraId());
            } else {
                Toast.makeText(this, "We need the camera permission.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    public void onPreviewFrame(byte[] data, int width, int height) {
        Log.v(TAG, width + "&" + height);
        mByteFlowRender.setRenderFrame(IMAGE_FORMAT_I420, data, width, height);
        mByteFlowRender.requestRender();

    }

    public void loadRGBAImage(int resId, int index) {
        InputStream is = this.getResources().openRawResource(resId);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                int bytes = bitmap.getByteCount();
                ByteBuffer buf = ByteBuffer.allocate(bytes);
                bitmap.copyPixelsToBuffer(buf);
                byte[] byteArray = buf.array();
                mByteFlowRender.loadLutImage(index, IMAGE_FORMAT_RGBA, bitmap.getWidth(), bitmap.getHeight(), byteArray);
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCaptureFrame(byte[] data, int width, int height) {
        ByteFlowFrame byteFlowFrame = new ByteFlowFrame(data, width, height);
        if (mReadPixelsReady) {
            mReadPixelsReady = false;
            mByteFlowRender.readPixels(new Size(byteFlowFrame.getHeight(), byteFlowFrame.getWidth()), getResultImgFile(".jpg").getPath());
        }
        mByteFlowRender.requestRender();

    }

    public File getResultImgFile(final String ext) {
        final File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, System.currentTimeMillis() + ext);

    }


    @Override
    public void onReadPixelsSaveToLocal(String imgPath) {
        mReadPixelsReady = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CameraActivity.this, "Save result image to path:" + imgPath, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
