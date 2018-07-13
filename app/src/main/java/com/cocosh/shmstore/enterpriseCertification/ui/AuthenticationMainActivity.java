package com.cocosh.shmstore.enterpriseCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.databinding.ActivityAuthenticationMainBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 引导页
 */

public class AuthenticationMainActivity extends BaseActivity {

    private ActivityAuthenticationMainBinding dataBinding;

    @Override
    public int setLayout() {

        return R.layout.activity_authentication_main;
    }

    @Override
    public void initView() {
        titleManager.defaultTitle("身份验证");
        dataBinding = getDataBinding();
        dataBinding.rlAgent.setOnClickListener(this);
        dataBinding.rlRepresent.setOnClickListener(this);
    }

    @Override
    public void onListener(@NotNull View view) {
        Intent it = new Intent(this, ScanIdCardActivity.class);
        switch (view.getId()) {
            case R.id.rlRepresent:
                it.putExtra("type", "法人代表");
                it.putExtra("isAgent",false);
                break;
            case R.id.rlAgent:
                it.putExtra("type", "代办人");
                it.putExtra("isAgent",true);
                break;
        }
        startActivity(it);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, AuthenticationMainActivity.class));
    }

    @Override
    public void reTryGetData() {

    }
}
