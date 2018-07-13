package com.cocosh.shmstore.forgetPsd.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.databinding.ActivityIdentifyMobileBinding;
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract;
import com.cocosh.shmstore.forgetPsd.presenter.IdentifyMoboilePresenter;
import com.cocosh.shmstore.utils.PermissionCode;
import com.cocosh.shmstore.utils.PermissionUtil;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

public class IdentifyMobileActivity extends BaseActivity implements IdentifyMobileContract.IView {

    private ActivityIdentifyMobileBinding dataBinding;
    private String phone;
    private boolean phoneOk;
    private boolean codeOk;
    private PermissionUtil permissionUtil;
    private IdentifyMoboilePresenter presenter = new IdentifyMoboilePresenter(IdentifyMobileActivity.this, this);

    @Override
    public int setLayout() {
        return R.layout.activity_identify_mobile;
    }

    @Override
    public void initView() {
        permissionUtil = new PermissionUtil(this);
        titleManager.defaultTitle("验证手机");
        dataBinding = getDataBinding();
        initClick();
        addListener();
    }

    private void addListener() {
        dataBinding.etIdentifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone = dataBinding.etPhone.getText().toString();
                if (phone.length() != 11 || charSequence.length() != 6) {
                    dataBinding.btNext.setEnabled(false);
                    dataBinding.btNext.setBackgroundColor(ContextCompat.getColor(IdentifyMobileActivity.this, R.color.grayBtn));
                } else {
                    dataBinding.btNext.setEnabled(true);
                    dataBinding.btNext.setBackgroundColor(ContextCompat.getColor(IdentifyMobileActivity.this, R.color.red));
                }
                if (charSequence.length() == 6) {
                    codeOk = true;
                } else {
                    codeOk = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dataBinding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    dataBinding.tbSend.setClick(true);
                    phoneOk = true;
                } else {
                    dataBinding.tbSend.setClick(false);
                    phoneOk = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initClick() {
        dataBinding.btNext.setOnClickListener(this);
        dataBinding.tvProblem.setOnClickListener(this);
        dataBinding.tbSend.setCallListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = dataBinding.etPhone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    presenter.sendCode(phone);
                }
            }
        });
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.btNext:
                if (phoneOk && codeOk) {
                    System.out.println(phoneOk + "-----" + codeOk);
                    presenter.checkCode(dataBinding.etPhone.getText().toString(), dataBinding.etIdentifyCode.getText().toString());
                }
                break;
            case R.id.tvProblem:
                showDialog();
                break;
        }

    }

    private void showDialog() {
        SmediaDialog dialog = new SmediaDialog(IdentifyMobileActivity.this);
        dialog.setTitle("首媒客服：01078334322");
        dialog.setPositiveText("拨打");
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionUtil.callPermission()) {
                    callPhone();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onCodeResult(@NotNull BaseModel<String> result) {
        if (result.getSuccess()) {
            dataBinding.tbSend.action();//验证码发送成功执行计时
        } else {
            ToastUtil.INSTANCE.show(result.getMessage());
        }
    }

    @Override
    public void onCheckedCodeResult(@NotNull BaseModel<String> result) {
        if (result.getSuccess()) {
            Intent intent = new Intent(IdentifyMobileActivity.this, SettingNewLoginPsdActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("token", result.getEntity());
            startActivityForResult(intent, 0);
        } else {
            ToastUtil.INSTANCE.show(result.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCode.PHONE.getType()) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone();
            }
        }
    }

    @Override
    public void reTryGetData() {

    }


    @SuppressLint("MissingPermission")
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01078334322"));
        startActivity(intent);
    }

}
