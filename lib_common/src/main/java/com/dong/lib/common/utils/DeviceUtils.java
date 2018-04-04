package com.dong.lib.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * ================================================
 * 获取设备常用信息和处理设备常用操作的工具类
 * Created by xiaoyulaoshi on 2018/3/23.
 * <p>
 * ================================================
 */

public class DeviceUtils {
    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取 IMEI 码
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return IMEI 码
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI() {
        TelephonyManager tm =
                (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : null;
    }
}
