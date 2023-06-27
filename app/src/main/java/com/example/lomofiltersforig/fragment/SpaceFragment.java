package com.example.lomofiltersforig.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.lomofiltersforig.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SeekBar seek_bar;
    private ImageView iv_picture;
    private SeekBar seek_bar_2;

    public SpaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpaceFragment newInstance(String param1, String param2) {
        SpaceFragment fragment = new SpaceFragment();
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
        View view = inflater.inflate(R.layout.fragment_space, container, false);
        seek_bar = (SeekBar) view.findViewById(R.id.seek_bar);
        Activity activity = getActivity();
        iv_picture = activity.findViewById(R.id.iv_picture);
        seek_bar_2 = (SeekBar)view.findViewById(R.id.seek_bar_2);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 根据SeekBar的进度值改变ImageView的大小
                int newWidth = iv_picture.getWidth() + (progress * 10);
                int newHeight = iv_picture.getHeight() + (progress * 10);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(newWidth, newHeight);
                iv_picture.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_bar_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newWidth = iv_picture.getWidth() + (-progress * 10);
                int newHeight = iv_picture.getHeight() + (-progress * 10);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(newWidth, newHeight);
                iv_picture.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;

    }
}