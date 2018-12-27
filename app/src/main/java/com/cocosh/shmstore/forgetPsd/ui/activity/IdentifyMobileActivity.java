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
import com.cocosh.shmstore.base.BaseBean;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.databinding.ActivityIdentifyMobileBinding;
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract;
import com.cocosh.shmstore.forgetPsd.presenter.IdentifyMoboilePresenter;
import com.cocosh.shmstore.sms.model.SMS;
import com.cocosh.shmstore.utils.PermissionCode;
import com.cocosh.shmstore.utils.PermissionUtil;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

//忘记密码
public class IdentifyMobileActivity extends BaseActivity implements IdentifyMobileContract.IView {

    private ActivityIdentifyMobileBinding dataBinding;
    private String phone;
    private boolean phoneOk;
    private boolean codeOk;
    private boolean passOK;
    private PermissionUtil permissionUtil;
    private IdentifyMoboilePresenter presenter = new IdentifyMoboilePresenter(IdentifyMobileActivity.this, this);

    @Override
    public int setLayout() {
        return R.layout.activity_identify_mobile;
    }

    @Override
    public void initView() {
        permissionUtil = new PermissionUtil(this);
        titleManager.defaultTitle("忘记密码");
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
                codeOk = charSequence.length() == 6;
                isOk();
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
                isOk();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        dataBinding.editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                passOK = s.length() >= 6;

                isOk();

            }

            @Override
            public void afterTextChanged(Editable s) {

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

                //确认密码弹窗
                SmediaDialog smDialog = new SmediaDialog(this);
                smDialog.showSmsMotifyPassword(dataBinding.editPass.getText().toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.forgetPass(dataBinding.etPhone.getText().toString(),
                                dataBinding.editPass.getText().toString(),
                                dataBinding.etIdentifyCode.getText().toString());
                    }
                });
                break;
            case R.id.tvProblem:
                showDialog();
                break;
        }

    }

    private void showDialog() {
        SmediaDialog dialog = new SmediaDialog(IdentifyMobileActivity.this);
        dialog.setTitle("客服：4009661168");
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
    public void onCodeResult(@NotNull BaseBean<SMS> result) {
        dataBinding.tbSend.action();//验证码发送成功执行计时
    }

    @Override
    public void onForgetPassResult(@NotNull BaseBean<String> result) {
        finish();
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
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4009661168"));
        startActivity(intent);
    }


    //输入条件检查
    public void isOk() {
        if (phoneOk && passOK && codeOk) {
            dataBinding.btNext.setClickable(true);
            dataBinding.btNext.setBackgroundResource(R.color.red);
        } else {
            dataBinding.btNext.setClickable(false);
            dataBinding.btNext.setBackgroundResource(R.color.grayBtn);
        }

    }
}
