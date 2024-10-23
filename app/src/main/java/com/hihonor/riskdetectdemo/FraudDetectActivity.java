/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hihonor.android.security.riskdetect.AuthRequest;
import com.hihonor.android.security.riskdetect.FraudDetect;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 反诈检测能力demo
 *
 * @since 2024-07-19
 */
public class FraudDetectActivity extends Activity {
    private static final String TAG = "FRAUD_DETECT";

    private static final String APP_ID = "104452002";

    private static final int SUCCEED = 0;

    private static final int FAILED = -1;

    private static final int JWS_PAYLOAD_LENGTH = 2;

    private static final int SPACES_FORMAT = 4;

    private static final int NONCE_LENGTH = 24;

    private FraudDetect mFraudDetect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fraud_detect);
        initView();
        AuthRequest authRequest = new AuthRequest();

        // 密码学nonce值
        authRequest.setNonce(getNonce());
        authRequest.setAppId(APP_ID);

        // 不需要对业务数据进行签名，alg字段传空字符串; 签名传算法填充方式，当前支持ECC256（“ecc256”）
        authRequest.setAlg("");
        mFraudDetect = new FraudDetect(authRequest);
    }

    private String getNonce() {
        byte[] nonce = new byte[NONCE_LENGTH];
        try {
            SecureRandom random;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                random = SecureRandom.getInstanceStrong();
            } else {
                random = SecureRandom.getInstance("SHA1PRNG");
            }
            random.nextBytes(nonce);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getNonce exception.");
        }
        return new String(nonce, StandardCharsets.UTF_8);
    }

    private void initView() {
        findViewById(R.id.btn_check_fraud).setOnClickListener(v -> {
            getFraudDetectResult();
        });
        findViewById(R.id.btn_feedback).setOnClickListener(v -> {
            provideFeedback();
        });
        findViewById(R.id.btn_get_api_tag).setOnClickListener(v -> {
            getFraudDetectApiTag();
        });
    }

    /**
     * 获得涉诈风险检测结果
     */
    private void getFraudDetectResult() {
        FraudDetect.FraudRiskResult fraudResult = mFraudDetect.getDetectResult();
        if (fraudResult.getErrorCode() == FAILED) {
            Log.e(TAG, "get fraud risk result failed.");
            return;
        }
        String jwsStr = fraudResult.getResultInfo();
        String[] jwsSplit = jwsStr.split("\\.");
        if (jwsSplit.length >= JWS_PAYLOAD_LENGTH) {
            if (TextUtils.isEmpty(jwsSplit[1])) {
                Log.e(TAG, "fraud risk result is empty.");
                return;
            }
            String payload = new String(Base64.getUrlDecoder().decode(jwsSplit[1].getBytes(StandardCharsets.UTF_8)));
            try {
                JSONObject jsonObject = new JSONObject(payload);
                int riskResult = jsonObject.getInt(FraudDetect.PAYLOAD_KEY_RESULT);
                Log.d(TAG, "fraud risk result: " + riskResult);
                JSONObject detailJson = jsonObject.getJSONObject(FraudDetect.PAYLOAD_KEY_DETAIL);
                Log.d(TAG, "detail info: " + detailJson.toString(SPACES_FORMAT));
            } catch (JSONException ex) {
                Log.e(TAG, "occur JSONException:" + ex.getMessage());
            }
        }
    }

    /**
     * 反馈风险处置结果
     */
    private void provideFeedback() {
        Gson gson = new Gson();
        FeedbackBean feedbackBean = new FeedbackBean(1);
        if (mFraudDetect.provideFeedbackInfo(gson.toJson(feedbackBean)) == SUCCEED) {
            Log.d(TAG, "sending feedback success");
        } else {
            Log.d(TAG, "sending feedback failed");
        }
    }

    /**
     * 获取反诈检测能力接口版本
     */
    private void getFraudDetectApiTag() {
        int tag = mFraudDetect.getFraudDetectApiTag();
        Log.d(TAG, "fraud detect api tag is:" + tag);
    }

    /**
     * feedback entity class
     *
     * @since 2024-07-19
     */
    private static class FeedbackBean {
        int disposalResult;

        FeedbackBean(int disposalResult) {
            this.disposalResult = disposalResult;
        }
    }
}
