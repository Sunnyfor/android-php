package com.cocosh.shmstore.newCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.databinding.ActivityConfirmInformationBinding;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.newCertification.model.Entity;
import com.cocosh.shmstore.newCertification.model.InforModel;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ConfirmInformationActivity extends BaseActivity {

    private ActivityConfirmInformationBinding dataBinding;

    @Override
    public int setLayout() {
        return R.layout.activity_confirm_information;
    }

    @Override
    public void initView() {
        dataBinding = getDataBinding();
        titleManager.defaultTitle(getString(R.string.newAuthen));
        initClick();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        ApiManager.INSTANCE.get(1, this, map, Constant.GETINFOR, new ApiManager.OnResult<InforModel>() {
            @Override
            public void onCatch(InforModel data) {

            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                ToastUtil.INSTANCE.show(e.getMessage());
            }

            @Override
            public void onSuccess(InforModel data) {
                if (data.getCode() == 200 && data.getSuccess()) {
                    addData(data);
                }
            }
        });
    }

    private void addData(InforModel data) {
        Entity dataEntity = data.getEntity();
        if (dataEntity != null) {

            dataBinding.address.setText(dataEntity.getCardAddress());
            String birth = dataEntity.getBirth();
            dataBinding.birth.setText((birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6)));
            dataBinding.name.setText(dataEntity.getRealName());
            dataBinding.date.setText((dataEntity.getValidityPeriodStartTime() + "至" + dataEntity.getValidityPeriodEndTime()));
            dataBinding.district.setText(dataEntity.getAddress());

            dataBinding.face.setText("1".equals(dataEntity.getFaceRecognition()) ? "已完善" : "未完善");
            dataBinding.idNum.setText(dataEntity.getIdNo());
            dataBinding.iussue.setText(dataEntity.getIssuingAgency());
            dataBinding.money.setText(("平台加盟费：￥" + dataEntity.getMoney()));
            dataBinding.nation.setText(dataEntity.getEthnic());
            dataBinding.sex.setText(dataEntity.getSex());
            SmApplication.Companion.getApp().setData("money", dataEntity.getMoney() + "");
            SmApplication.Companion.getApp().setData("bizCode", dataEntity.getBizCode());
        }

    }

    private void initClick() {
        dataBinding.tvModify.setOnClickListener(this);
        dataBinding.tvPay.setOnClickListener(this);
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.tvModify:
                SmediaDialog dialog = new SmediaDialog(ConfirmInformationActivity.this);
                dialog.setTitle("修改资料将重新提交审核\n确定修改资料吗？");
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ConfirmInformationActivity.this, AuthorGuideActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;
            case R.id.tvPay:
                Intent intent = new Intent(ConfirmInformationActivity.this, PayActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, ConfirmInformationActivity.class));
    }


    @Override
    public void reTryGetData() {
        initData();
    }
}
