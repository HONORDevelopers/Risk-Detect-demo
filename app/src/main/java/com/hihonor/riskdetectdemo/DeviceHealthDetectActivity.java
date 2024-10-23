/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hihonor.android.security.riskdetect.DeviceHealthDetect;

/**
 * 设备健康检测demo
 *
 * @since 2024-07-15
 */
public class DeviceHealthDetectActivity extends Activity {
    private static final String TAG = "DEVICE_HEALTH_DETECT";

    private static final int ERROR_FAILED = -1;

    private static final int DEVICE_HEALTHY = 0;

    private static final int DEVICE_UNHEALTHY = 1;

    private DeviceHealthDetect deviceHealthDetect = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_health_detect);
        initView();
        initDeviceDetect();
    }

    private void initView() {
        findViewById(R.id.getDetectResult).setOnClickListener(v -> {
            getDetectResult();
        });
        findViewById(R.id.getDeviceHealthDetectApiTag).setOnClickListener(v -> {
            getDeviceHealthDetectApiTag();
        });
    }

    private void initDeviceDetect() {
        deviceHealthDetect = new DeviceHealthDetect();
        if (deviceHealthDetect == null) {
            Log.e(TAG, "This device does not support DeviceHealthDetect");
            return;
        }
    }

    /**
     * 获取设备完整性检测结果
     */
    private void getDetectResult() {
        if (deviceHealthDetect == null) {
            Log.e(TAG, "This device does not support getDetectResult");
            return;
        }
        DeviceHealthDetect.DeviceHealthDetectResult deviceHealthDetectResult = deviceHealthDetect.getDetectResult();
        if (deviceHealthDetectResult.getErrorCode() == ERROR_FAILED
                || deviceHealthDetectResult.getResult() == ERROR_FAILED) {
            // 检测失败
            Log.e(TAG, "detect failed");
        } else if (deviceHealthDetectResult.getResult() == DEVICE_UNHEALTHY) {
            // 设备处于不健康状态
            Log.e(TAG, "device unhealthy");
        } else if (deviceHealthDetectResult.getResult() == DEVICE_HEALTHY) {
            // 设备处于健康状态
            Log.e(TAG, "device healthy");
        } else {
            Log.e(TAG, "unrecognized status");
        }

    String msg = "result: " + deviceHealthDetectResult.getResult()
                + ", errorCode: " + deviceHealthDetectResult.getErrorCode()
                + ", resultInfo: " + deviceHealthDetectResult.getResultInfo(); // resultInfo 结果调用成功或失败的提示信息。
        Log.i(TAG, msg);
    }

    /**
     * 获取设备健康检测能力接口版本
     */
    private void getDeviceHealthDetectApiTag() {
        if (deviceHealthDetect == null) {
            Log.e(TAG, "This device does not support get DeviceHealthDetectApiTag");
            return;
        }
        int apiTag = deviceHealthDetect.getDeviceHealthDetectApiTag();
        String msg = "DeviceHealthDetectApiTag: " + apiTag;
        Log.i(TAG, msg);
    }
}
