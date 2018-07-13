package com.cocosh.shmstore.newCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.databinding.ActivityIdcardBinding;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

public class IDcardActivity extends BaseActivity {

    private ActivityIdcardBinding dataBinding;

    @Override
    public int setLayout() {
        return R.layout.activity_idcard;
    }

    @Override
    public void initView() {
        titleManager.defaultTitle("身份证");
        dataBinding = getDataBinding();

        String front = SmApplication.Companion.getApp().getData(DataCode.ID_FRONT, false);
        String back = SmApplication.Companion.getApp().getData(DataCode.ID_BACK, false);

        GlideUtils.load(this, front, dataBinding.ivFront);
        GlideUtils.load(this, back, dataBinding.ivBack);

    }

    @Override
    public void onListener(@NotNull View view) {

    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, IDcardActivity.class));
    }

    @Override
    public void reTryGetData() {

    }
}
