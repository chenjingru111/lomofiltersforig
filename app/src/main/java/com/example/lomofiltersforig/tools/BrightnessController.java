package com.example.lomofiltersforig.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class BrightnessController {

    private Context mContext;
    private ContentResolver mContentResolver;

    public BrightnessController(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    // 获取当前屏幕亮度值
    public int getBrightness() {
        int brightness = 0;
        try {
            brightness = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }

    // 设置屏幕亮度值
    public void setBrightness(int brightnessValue) {
        // 需要修改系统设置权限，否则会抛出异常
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
    }
}