package com.cocosh.shmstore.forgetPsd.ui.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.databinding.ActivitySettingLoginPsdBinding;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;
import com.umeng.socialize.media.Base;

import org.jetbrains.annotations.NotNull;

public class SettingNewLoginPsdActivity extends BaseActivity {

    private ActivitySettingLoginPsdBinding dataBinding;
    private boolean isPsdShow;

    @Override
    public int setLayout() {
        return R.layout.activity_setting_login_psd;
    }

    @Override
    public void initView() {
        dataBinding = getDataBinding();
        titleManager.defaultTitle("设置新密码");
        initListener();
        dataBinding.btComplete.setOnClickListener(this);
    }

    private void initListener() {
        dataBinding.etSetPsd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //获取焦点
                    dataBinding.rlPsd.setVisibility(View.VISIBLE);

                } else {
                    //没有获取焦点
                    dataBinding.rlPsd.setVisibility(View.INVISIBLE);
                }
            }
        });
        dataBinding.rlPsd.setOnClickListener(this);

        dataBinding.etSetPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 20 && charSequence.length() >= 6) {
                    dataBinding.btComplete.setEnabled(true);
                    dataBinding.btComplete.setBackgroundColor(ContextCompat.getColor(SettingNewLoginPsdActivity.this, R.color.red));
                } else {
                    dataBinding.btComplete.setEnabled(false);
                    dataBinding.btComplete.setBackgroundColor(Color.parseColor("#C5C5C5"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.rlPsd:
                isPsdShow = !isPsdShow;
                if (isPsdShow) {
                    //显示
                    dataBinding.etSetPsd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    dataBinding.itvShowPsd.setVisibility(View.VISIBLE);
                    dataBinding.itvHidePsd.setVisibility(View.INVISIBLE);
                } else {
                    //隐藏
                    dataBinding.etSetPsd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    dataBinding.itvShowPsd.setVisibility(View.INVISIBLE);
                    dataBinding.itvHidePsd.setVisibility(View.VISIBLE);
                }
                dataBinding.etSetPsd.setSelection(dataBinding.etSetPsd.getText().length());
                break;
            case R.id.btComplete:
                SmediaDialog smediaDialog = new SmediaDialog(this);
                smediaDialog.showSmsMotifyPassword(
                        getIntent().getStringExtra("phone"),
                        dataBinding.etSetPsd.getText().toString(),
                        getIntent().getStringExtra("token"),
                        new ApiManager.OnResult<BaseModel<String>>() {
                            @Override
                            public void onSuccess(BaseModel<String> data) {
                                if (data.getSuccess()) {
                                    setResult(0);
                                    finish();
                                } else {
                                    ToastUtil.INSTANCE.show(data.getMessage());
                                }
                            }

                            @Override
                            public void onFailed(@NotNull Throwable e) {

                            }

                            @Override
                            public void onCatch(BaseModel<String> data) {

                            }
                        }
                );

                break;

        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingNewLoginPsdActivity.this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("密码设置成功！");
        alertDialog.show();
    }

    @Override
    public void reTryGetData() {

    }
}
