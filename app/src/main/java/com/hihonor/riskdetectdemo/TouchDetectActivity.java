/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hihonor.android.security.riskdetect.TouchDetect;

/**
 * 模拟触屏检测demo
 *
 * @since 2024-07-15
 */
public class TouchDetectActivity extends Activity {
    private static final String TAG = "TOUCH_DETECT";

    private static final int FAILED = -1;

    private static final int NOT_START = -2;

    private static final int NO_DATA = -6;

    private static final float RESULT_ZERO = 0;

    private TouchDetect touchDetect = new TouchDetect();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_detect);
        initView();
        initTouchDetect();
    }

    private void initView() {
        findViewById(R.id.startDetect).setOnClickListener(v -> {
            startDetect();
        });
        findViewById(R.id.stopDetect).setOnClickListener(v -> {
            stopDetect();
        });
        findViewById(R.id.getDetectResult).setOnClickListener(v -> {
            getDetectResult();
        });
        findViewById(R.id.getTouchDetectApiTag).setOnClickListener(v -> {
            getTouchDetectApiTag();
        });
    }

    private void initTouchDetect() {
        touchDetect = new TouchDetect();
        if (touchDetect == null) {
            Log.e(TAG, "This device does not support stop TouchDetect");
            return;
        }
    }

    /**
     * 获取模拟触屏检测结果
     */
    private void getDetectResult() {
        if (touchDetect == null) {
            Log.e(TAG, "This device does not support getDetectResult");
            return;
        }
        TouchDetect.TouchDetectResult touchDetectResult = touchDetect.getDetectResult();
        if (touchDetectResult.getErrorCode() == FAILED) {
            Log.e(TAG, "get touch detect result failed");
        } else if (touchDetectResult.getErrorCode() == NOT_START) {
            // 模拟触屏检测能力需要首先执行开启操作，使用完毕释放相关的资源
            Log.d(TAG, "need startDetect first");
        } else if (touchDetectResult.getResult() == NO_DATA) {
            // 未检测到设备存在触屏操作，进行触屏操作后再获取检测结果。
            Log.d(TAG, "no touch data, please touch screen first");
        } else if (touchDetectResult.getResult() < RESULT_ZERO) {
            // 调用存在异常，可尝试再次获取检测结果
            Log.e(TAG, "exception occurred, please try getDetectResult again");
        } else {
            // result 0-100: bot检测结果，值越大，越接近用户行为
            Log.d(TAG, "get touch detect result:" + touchDetectResult.getResult());
        }

        String msg = "errorCode: " + touchDetectResult.getErrorCode()
                + ", result: " + touchDetectResult.getResult()
                + ", resultInfo: " + touchDetectResult.getResultInfo(); // resultInfo 结果调用成功或失败的提示信息。
        Log.e(TAG, msg);
    }

    /**
     * 开启模拟触屏检测能力
     * 调用getDetectResult前，需要先提前开启模拟触屏检测能力。
     */
    private void startDetect() {
        if (touchDetect == null) {
            Log.e(TAG, "This device does not support start TouchDetect");
            return;
        }
        int ret = touchDetect.startDetect();
        if (ret == FAILED) {
            Log.e(TAG, "start touch detect failed");
            return;
        }
        Log.i(TAG, "start touch detect success");
    }

    /**
     * 关闭模拟触屏检测能力
     * 获取检测结果后，关闭模拟触屏检测能力并释放资源。
     */
    private void stopDetect() {
        if (touchDetect == null) {
            Log.e(TAG, "This device does not support stop TouchDetect");
            return;
        }
        touchDetect.stopDetect();
        Log.i(TAG, "stop touch detect success");
    }

    /**
     * 获取模拟触屏检测能力接口版本
     */
    private void getTouchDetectApiTag() {
        if (touchDetect == null) {
            Log.e(TAG, "This device does not support get TouchDetectApiTag");
            return;
        }
        int apiTag = touchDetect.getTouchDetectApiTag();
        Log.i(TAG, "TouchDetectApiTag: " + apiTag);
    }
}
