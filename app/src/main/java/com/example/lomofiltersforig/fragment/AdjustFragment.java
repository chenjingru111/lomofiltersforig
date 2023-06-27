package com.example.lomofiltersforig.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.lomofiltersforig.activity.CollageActivity;
import com.example.lomofiltersforig.activity.FullScreenActivity;
import com.example.lomofiltersforig.activity.HalffullScreenActivity;
import com.example.lomofiltersforig.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdjustFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdjustFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout ll_swap;
    private LinearLayout ll_delete;
    private LinearLayout ll_fill;
    private LinearLayout ll_inside;
    private LinearLayout ll_filter;
    private LinearLayout ll_left;
    private LinearLayout ll_right;
    private LinearLayout ll_horizontal;

    int a=0;
    public AdjustFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdjustFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdjustFragment newInstance(String param1, String param2) {
        AdjustFragment fragment = new AdjustFragment();
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
        View view = inflater.inflate(R.layout.fragment_adjust, container, false);
        ll_swap = (LinearLayout) view.findViewById(R.id.ll_Swap);
        ll_delete = (LinearLayout) view.findViewById(R.id.ll_Delete);
        ll_fill = (LinearLayout) view.findViewById(R.id.ll_Fill);
        ll_inside = (LinearLayout) view.findViewById(R.id.ll_Inside);
        ll_filter = (LinearLayout) view.findViewById(R.id.ll_Filter);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_Left);
        ll_right = (LinearLayout) view.findViewById(R.id.ll_Right);
        ll_horizontal = (LinearLayout) view.findViewById(R.id.ll_Horizontal);


        ll_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnPicture(180);
            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                ImageView imageView = activity.findViewById(R.id.iv_picture);
                imageView.setImageDrawable(null);
                activity.finish();
                startActivity(new Intent(getActivity(), CollageActivity.class));
            }
        });
        ll_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    // 查找当前 Activity 中的 ImageView
                    ImageView imageView = activity.findViewById(R.id.iv_picture);
                    if (imageView != null) {
                        Drawable drawable = imageView.getDrawable(); // 获取 ImageView 中的 Drawable
                        if (drawable != null) { // 判断 Drawable 是否为空
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap
                            String s = saveBitmapToGallery(activity, bitmap);
                            Intent intent = new Intent(getActivity(), FullScreenActivity.class);
                            intent.putExtra("pic",s);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        ll_inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    // 查找当前 Activity 中的 ImageView
                    ImageView imageView = activity.findViewById(R.id.iv_picture);
                    if (imageView != null) {
                        Drawable drawable = imageView.getDrawable(); // 获取 ImageView 中的 Drawable
                        if (drawable != null) { // 判断 Drawable 是否为空
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap
                            String s = saveBitmapToGallery(activity, bitmap);
                            Intent intent = new Intent(getActivity(), HalffullScreenActivity.class);
                            intent.putExtra("half_pic",s);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a==0){
                    Activity activity = getActivity();
                    SeekBar mSeekBar = activity.findViewById(R.id.brightness_seekbar);
                    mSeekBar.setVisibility(View.VISIBLE);
                    a=1;
                }else {
                    Activity activity = getActivity();
                    SeekBar mSeekBar = activity.findViewById(R.id.brightness_seekbar);
                    mSeekBar.setVisibility(View.INVISIBLE);
                    a=0;
                }
            }
        });
        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnPicture(270);
            }
        });
        ll_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnPicture(90);
            }
        });
        ll_horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                ImageView imageView = activity.findViewById(R.id.iv_picture);
                Drawable drawable = imageView.getDrawable(); // 获取 ImageView 中的 Drawable
                if (drawable != null) { // 判断 Drawable 是否为空
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap

                    Matrix mirrorMatrix = new Matrix();
                    mirrorMatrix.setScale(-1, 1); // 水平反转
                    Bitmap mirrorBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mirrorMatrix, false);
                    imageView.setImageBitmap(mirrorBitmap);
                    activity.findViewById(R.id.iv_load).setOnClickListener(v1 -> {
                        saveBitmapToGallery(activity,mirrorBitmap);
                    });
                }
            }
        });
        return view;
    }

    private void turnPicture(int a){
        Activity activity = getActivity();
        if (activity != null) {
            // 查找当前 Activity 中的 ImageView
            ImageView imageView = activity.findViewById(R.id.iv_picture);
            if (imageView != null) {
                Drawable drawable = imageView.getDrawable(); // 获取 ImageView 中的 Drawable
                if (drawable != null) { // 判断 Drawable 是否为空
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap(); // 将 Drawable 转换为 Bitmap
                    // 创建一个矩阵并通过矩阵将 Bitmap 顺时针旋转180度
                    Matrix matrix = new Matrix();
                    matrix.postRotate(a);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    // 将旋转后的 Bitmap 显示在 ImageView 中
                    imageView.setImageBitmap(rotatedBitmap);

                    activity.findViewById(R.id.iv_load).setOnClickListener(v -> {
                        saveBitmapToGallery(activity,rotatedBitmap);
                    });

                }
            }
        }
    }

    public static String saveBitmapToGallery(Context context, Bitmap bitmap) {
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dissmiss","firem");
    }
}