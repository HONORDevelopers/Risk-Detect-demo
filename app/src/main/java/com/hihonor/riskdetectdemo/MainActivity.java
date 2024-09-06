/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * DemoApk主界面
 *
 * @since 2024-08-12
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_device_health_detect)
            .setOnClickListener(v -> startActivity(new Intent(this, DeviceHealthDetectActivity.class)));
        findViewById(R.id.btn_touch_detect)
            .setOnClickListener(v -> startActivity(new Intent(this, TouchDetectActivity.class)));
        findViewById(R.id.btn_risk_app_detect)
            .setOnClickListener(v -> startActivity(new Intent(this, RiskAppDetectActivity.class)));
        findViewById(R.id.btn_fraud_detect)
            .setOnClickListener(v -> startActivity(new Intent(this, FraudDetectActivity.class)));
        findViewById(R.id.btn_app_identity)
            .setOnClickListener(v -> startActivity(new Intent(this, AppIdentityActivity.class)));
    }
}