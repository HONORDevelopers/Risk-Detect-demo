/*
 * Copyright (c) Honor Device Co., Ltd. 2024-2024. All rights reserved.
 */

package com.hihonor.riskdetectdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.hihonor.android.security.riskdetect.AuthRequest;
import com.hihonor.android.security.riskdetect.FraudDetect;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 应用身份证明能力demo
 *
 * @since 2024-07-19
 */
public class AppIdentityActivity extends Activity {
    private static final String TAG = "AppIdentityActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_identity_layout);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_check).setOnClickListener(v -> {
            try {
                getAppIdentityResult();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /*
    * @result  获取包含apk签名信息的JWS
    * */
    private void getAppIdentityResult() throws JSONException {
        TextView view = findViewById(R.id.tv_result);
        byte[] nonce = new byte[24];
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        random.nextBytes(nonce);
        String hexString = bytesToBase64(nonce);
        StringBuilder stringBuilder = new StringBuilder();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setNonce(hexString);
        authRequest.setAlg("ecc256");
        FraudDetect fraudDetect = new FraudDetect(authRequest);
        FraudDetect.FraudRiskResult appIdentityResult = null;
        try {
            appIdentityResult = fraudDetect.getDetectResult();
        } catch (Exception e) {
            Log.e(TAG, "getDetectResult exception: " + e.getMessage());
        }
        if (appIdentityResult != null) {
            Log.e(TAG, "FraudRiskResult: errorCode: " + appIdentityResult.getErrorCode() +
                    ", result: " + appIdentityResult.getResultInfo());
            stringBuilder.append("errorCode: ").append(appIdentityResult.getErrorCode());
        }
        if (appIdentityResult != null && appIdentityResult.getErrorCode() != -1) {
            String jwsStr = appIdentityResult.getResultInfo();
            if (TextUtils.isEmpty(jwsStr)) {
                Toast.makeText(this, "ResultInfo is empty!!", Toast.LENGTH_SHORT).show();
                view.setText("获取包含apk签名信息的JWS失败:" + stringBuilder);
                return;
            }
            String[] jwsSplit = jwsStr.split("\\.");
            if (jwsSplit.length >= 2) {
                String payload = new String(Base64.getDecoder().decode(jwsSplit[1].getBytes()));
                JSONObject jsonObject = new JSONObject(payload);
                payload = jsonObject.toString(4);
                stringBuilder.append(payload);
            }
        }
        view.setText("获取包含apk签名信息的JWS : " + stringBuilder);
    }

}
