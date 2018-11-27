package com.cocosh.shmstore.login.ui.activity;

import android.content.Intent;
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
import com.cocosh.shmstore.newhome.HomeActivity;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.login.model.Login;
import com.cocosh.shmstore.utils.IntentCode;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.utils.UserManager;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SettingLoginPsdActivity extends BaseActivity {

    private ActivitySettingLoginPsdBinding dataBinding;
    private boolean isPsdShow;
    private Login login;

    @Override
    public int setLayout() {
        return R.layout.activity_setting_login_psd;
    }

    @Override
    public void initView() {
        titleManager.defaultTitle("设置登录密码");
        dataBinding = getDataBinding();
        login = UserManager.INSTANCE.getLogin();
        initListener();

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
                    dataBinding.btComplete.setBackgroundColor(ContextCompat.getColor(SettingLoginPsdActivity.this, R.color.red));
                } else {
                    dataBinding.btComplete.setEnabled(false);
                    dataBinding.btComplete.setBackgroundColor(Color.parseColor("#C5C5C5"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dataBinding.btComplete.setOnClickListener(this);
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.rlPsd:
                isPsdShow = !isPsdShow;
                if (isPsdShow) {
                    //显示
                    dataBinding.etSetPsd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    dataBinding.itvHidePsd.setVisibility(View.INVISIBLE);
                    dataBinding.itvShowPsd.setVisibility(View.VISIBLE);
                    dataBinding.etSetPsd.setSelection(dataBinding.etSetPsd.getText().length());
                } else {
                    //隐藏
                    dataBinding.etSetPsd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    dataBinding.itvHidePsd.setVisibility(View.VISIBLE);
                    dataBinding.itvShowPsd.setVisibility(View.INVISIBLE);
                    dataBinding.etSetPsd.setSelection(dataBinding.etSetPsd.getText().length());
                }
                break;

            case R.id.btComplete:

                SmediaDialog smediaDialog = new SmediaDialog(this);
                smediaDialog.setTitle(getResources().getString(R.string.surePass));
                smediaDialog.setDesc(dataBinding.etSetPsd.getText().toString());
                smediaDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> paramas = new HashMap<>();
                        paramas.put("userName", getIntent().getStringExtra("phone"));
                        paramas.put("userPwd", dataBinding.etSetPsd.getText().toString());
                        paramas.put("token", login.getToken());
                        paramas.put("type", login.getType());
                        paramas.put("openId", login.getOpenId());
                        paramas.put("deviceType", "android");

                        ApiManager.INSTANCE.post(SettingLoginPsdActivity.this, paramas, Constant.WEB_REGISTER, new ApiManager.OnResult<BaseModel<Login>>() {
                            @Override
                            public void onSuccess(BaseModel<Login> data) {
                                if (data.getSuccess()) {
                                    if (data.getEntity() != null) {
                                        UserManager.INSTANCE.setLogin(data.getEntity());
                                        startActivity(new Intent(SettingLoginPsdActivity.this, HomeActivity.class));
                                        setResult(IntentCode.IS_REGIST);
                                        finish();
                                    }
                                } else {
                                    ToastUtil.INSTANCE.show(data.getMessage());
                                }
                            }

                            @Override
                            public void onFailed(@NotNull Throwable e) {

                            }

                            @Override
                            public void onCatch(BaseModel<Login> data) {

                            }
                        });
                    }
                });
                smediaDialog.show();
        }
    }

    @Override
    public void reTryGetData() {

    }
}
