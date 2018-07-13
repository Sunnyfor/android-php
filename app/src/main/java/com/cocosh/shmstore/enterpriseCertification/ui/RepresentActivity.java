package com.cocosh.shmstore.enterpriseCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

/**
 * 法人代表身份信息
 */
public class RepresentActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_represent;
    }

    @Override
    public void initView() {
        titleManager.defaultTitle("身份验证");

    }

    @Override
    public void onListener(@NotNull View view) {

    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, RepresentActivity.class));
    }

    @Override
    public void reTryGetData() {

    }
}
