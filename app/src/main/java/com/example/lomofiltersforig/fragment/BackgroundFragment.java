package com.example.lomofiltersforig.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lomofiltersforig.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackgroundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private ImageView iv_4;
    private ImageView iv_5;
    private ImageView iv_6;
    private ImageView iv_7;
    private RelativeLayout rl_changebackground;
    private ImageView iv_picture;
    private TextView iv_load;

    public BackgroundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackgroundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BackgroundFragment newInstance(String param1, String param2) {
        BackgroundFragment fragment = new BackgroundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_background, container, false);
        iv_1 = (ImageView) view.findViewById(R.id.iv_1);
        iv_2 = (ImageView) view.findViewById(R.id.iv_2);
        iv_3 = (ImageView) view.findViewById(R.id.iv_3);
        iv_4 = (ImageView) view.findViewById(R.id.iv_4);
        iv_5 = (ImageView) view.findViewById(R.id.iv_5);
        iv_6 = (ImageView) view.findViewById(R.id.iv_6);
        iv_7 = (ImageView) view.findViewById(R.id.iv_7);

        Activity activity = getActivity();
        rl_changebackground = activity.findViewById(R.id.rl_changebackground);
        iv_load = activity.findViewById(R.id.iv_load);
        iv_picture = activity.findViewById(R.id.iv_picture);
        iv_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap bitmap = BackGroundController.captureSurfaceView(surface_view);
                Bitmap bitmap = captureBackgroundAndPicture();
                saveBitmapToGallery_background(getActivity(),bitmap);
                //}


            }
        });

        iv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        iv_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture(v);
            }
        });
        return view;
    }

    private void setPicture(View view){
        switch (view.getId()){
            case R.id.iv_1:
               rl_changebackground.setBackgroundResource(R.drawable.pic_1);
               break;
            case R.id.iv_2:
                rl_changebackground.setBackgroundResource(R.drawable.pic_2);
                break;
            case R.id.iv_3:
                rl_changebackground.setBackgroundResource(R.drawable.pic_3);
                break;
            case R.id.iv_4:
                rl_changebackground.setBackgroundResource(R.drawable.pic_4);
                break;
            case R.id.iv_5:
                rl_changebackground.setBackgroundResource(R.drawable.pic_5);
                break;
            case R.id.iv_6:
                rl_changebackground.setBackgroundResource(R.drawable.pic_6);
                break;
            case R.id.iv_7:
                rl_changebackground.setBackgroundResource(R.drawable.pic_7);
                break;
        }
    }

    public static String saveBitmapToGallery_background(Context context, Bitmap mergedBitmap) {
        // 首先创建一个保存目录
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 指定文件路径和名称
        File file = new File(dir, fileName);
        try {
            // 将 Bitmap 保存到文件中
            FileOutputStream out = new FileOutputStream(file);
            mergedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // 通知系统扫描文件
            Uri uri = Uri.fromFile(file);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(mediaScanIntent);
            // 弹出保存成功提示
            Toast.makeText(context, "succeed to save", Toast.LENGTH_SHORT).show();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return fileName;
        }
    }


    public Bitmap captureBackgroundAndPicture() {
        // 创建一个Bitmap对象并将Canvas绘制到上面
        Bitmap bitmap = Bitmap.createBitmap(rl_changebackground.getWidth(), rl_changebackground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 在Canvas上绘制背景视图
        rl_changebackground.draw(canvas);

        // 计算图片视图在Canvas中的位置
        int[] location = new int[2];
        iv_picture.getLocationOnScreen(location);

        // 在Canvas上绘制图片视图
        canvas.save();
        canvas.translate(0, rl_changebackground.getHeight());
        canvas.clipRect(location[0], location[1] - rl_changebackground.getHeight(), location[0] + iv_picture.getWidth(), location[1]);
        iv_picture.draw(canvas);
        canvas.restore();

        return Bitmap.createScaledBitmap(bitmap, rl_changebackground.getWidth(), rl_changebackground.getHeight(), true);
    }

}