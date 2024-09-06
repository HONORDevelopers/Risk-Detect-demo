/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hihonor.android.security.riskdetect.RiskAppDetect;

/**
 * 前序欺诈应用检测demo
 *
 * @since 2024-07-18
 */
public class RiskAppDetectActivity extends Activity {
    private static final String TAG = "RISK_APP_DETECT";

    private static final int SUCCEED = 0;

    private static final int EXIST_RISKAPP = 1;

    private static final int ZERO_RISKAPP = 0;

    private RiskAppDetect mRiskAppDetect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_app_detect);
        initView();
        mRiskAppDetect = new RiskAppDetect();
    }

    private void initView() {
        findViewById(R.id.btn_get_detect_result).setOnClickListener(v -> {
            getDetectResult();
        });
        findViewById(R.id.btn_get_api_tag).setOnClickListener(v -> {
            getRiskAppDetectApiTag();
        });
    }

    /**
     * 获得前序欺诈应用检测结果
     */
    private void getDetectResult() {
        RiskAppDetect.RiskAppDetectResult riskAppResult = mRiskAppDetect.getDetectResult();
        if (riskAppResult.getErrorCode() == SUCCEED) {
            Log.d(TAG, "risk app result:" + riskAppResult.getResult());
            if (riskAppResult.getResult() == EXIST_RISKAPP) {
                Log.d(TAG, "has pre-order fraud application.");
            } else if (riskAppResult.getResult() == ZERO_RISKAPP) {
                Log.d(TAG, "none pre-order fraud application.");
            }
        } else {
            Log.e(TAG, "get risk app result failed.");
        }
    }

    /**
     * 获取前序欺诈应用检测能力接口版本
     */
    private void getRiskAppDetectApiTag() {
        int tag = mRiskAppDetect.getRiskAppDetectApiTag();
        Log.d(TAG, "risk app api tag is:" + tag);
    }
}
