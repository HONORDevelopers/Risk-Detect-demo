/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hihonor.android.security.riskdetect.AccessibilityControl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 无障碍管控demo
 *
 * @since 2024-08-12
 */
public class AccessibilityControlActivity extends Activity {
    private static final String TAG = "AccessibilityControlTag";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private AccessibilityControl mAccessibilityControl = new AccessibilityControl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_control);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_set_white_list).setOnClickListener(v -> setWhiteList());
        findViewById(R.id.btn_start_control).setOnClickListener(v -> startControl());
        findViewById(R.id.btn_stop_control).setOnClickListener(v -> stopControl());
        findViewById(R.id.btn_get_status).setOnClickListener(v -> getStatus());
        findViewById(R.id.btn_get_api_tag).setOnClickListener(v -> getApiTag());
    }

    /**
     * 设置三方应用白名单（非必须）
     */
    private void setWhiteList() {
        EXECUTOR_SERVICE.execute(() -> {
            Map<String, String> map = new ArrayMap<>();
            map.put("com.hihonor.riskdetectdemo", "FC99FAEDBC86D73B37542AE66409E3C9CC62820097191F2C911B53D091C0AE6E");
            int result = mAccessibilityControl.setPkgList(map);
            Log.i(TAG, "setWhiteList: result is " + result);
            switch (result) {
                case AccessibilityControl.SUCCESS:
                    // 三方应用白名单设置成功
                    Log.i(TAG, "setWhiteList: setWhiteList success.");
                    break;
                case AccessibilityControl.PERMISSION_DENIED:
                    // kit鉴权失败，需要在荣耀开发者网站上申请风险检测服务权限
                    Log.e(TAG, "setWhiteList: permission denied.");
                    break;
                case AccessibilityControl.DATA_EXCEED:
                    // 数据过多，控制传入的包名在200个以内
                    Log.e(TAG, "setWhiteList: data exceed.");
                    break;
                case AccessibilityControl.ERROR_FAILED:
                    // 其他原因失败，建议重新初始化再尝试一次
                    Log.e(TAG, "setWhiteList: unknown failure.");
                    mAccessibilityControl = new AccessibilityControl();
                    mAccessibilityControl.setPkgList(map);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 启动无障碍管控
     */
    private void startControl() {
        // 除获取版本号以外的方法都需要放在子线程中执行，否则通过点击按钮触发事件时会被屏幕朗读行为阻塞
        // 由于这些接口都是耗时操作，所以正常业务中也建议在子线程中调用
        EXECUTOR_SERVICE.execute(() -> {
            int result = mAccessibilityControl.enableControl();
            Log.i(TAG, "startControl: result is " + result);
            switch (result) {
                case AccessibilityControl.SUCCESS:
                    // 启动无障碍管控成功
                    // 当判断不再需要管控时，及时停止管控
                    Log.i(TAG, "startControl: startControl success.");
                    break;
                case AccessibilityControl.PERMISSION_DENIED:
                    // kit鉴权失败，需要在荣耀开发者网站上申请风险检测服务权限
                    Log.e(TAG, "startControl: permission denied.");
                    break;
                case AccessibilityControl.ERROR_CONFLICT:
                    // 当前已有应用启动管控，可能是其他应用也可能是自己
                    // 建议在用户进行自己应用的界面操作时再重新调用。如果仍返回该错误码，说明自己之前已经启动管控
                    Log.e(TAG, "startControl: startControl conflict.");
                    break;
                case AccessibilityControl.NOT_FOREGROUND:
                    // 一种情况是当前应用未处于前台，待切换到前台再启动管控
                    // 另一种情况是分屏场景下，应用处于前台，但焦点未在自己应用的界面。此时建议在用户进行自己应用的界面操作时再重新调用
                    Log.e(TAG, "startControl: not foreground.");
                    break;
                case AccessibilityControl.ERROR_FAILED:
                    // 其他原因失败，建议重新初始化再尝试一次
                    Log.e(TAG, "startControl: unknown failure.");
                    mAccessibilityControl = new AccessibilityControl();
                    mAccessibilityControl.enableControl();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 停止无障碍管控
     */
    private void stopControl() {
        // 除获取版本号以外的方法都需要放在子线程中执行，否则通过点击按钮触发事件时会被屏幕朗读行为阻塞
        // 由于这些接口都是耗时操作，所以正常业务中也建议在子线程中调用
        EXECUTOR_SERVICE.execute(() -> {
            int result = mAccessibilityControl.disableControl();
            Log.i(TAG, "stopControl: result is " + result);
            switch (result) {
                case AccessibilityControl.SUCCESS:
                    // 停止无障碍管控成功
                    Log.i(TAG, "stopControl: stopControl success.");
                    break;
                case AccessibilityControl.PERMISSION_DENIED:
                    // kit鉴权失败，需要在荣耀开发者网站上申请风险检测服务权限
                    Log.e(TAG, "stopControl: permission denied.");
                    break;
                case AccessibilityControl.ERROR_FAILED:
                    // 其他原因失败，建议重新初始化再尝试一次
                    Log.e(TAG, "stopControl: unknown failure.");
                    mAccessibilityControl = new AccessibilityControl();
                    mAccessibilityControl.disableControl();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 获取当前无障碍管控状态。建议在启动管控前先调用这个方法进行判断。
     */
    private void getStatus() {
        EXECUTOR_SERVICE.execute(() -> {
            boolean result = mAccessibilityControl.getControlStatus();
            Log.i(TAG, "getStatus: result is " + result);
            if (!result) {
                // 当前无应用在管控中，可启动管控
                Log.i(TAG, "getStatus: no current control.");
            }
        });
    }

    /**
     * 获取无障碍管控服务版本号
     */
    private void getApiTag() {
        int version = AccessibilityControl.getVersion();
        Log.i(TAG, "getApiTag: api tag is " + version);
    }
}
