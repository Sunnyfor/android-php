package com.cocosh.shmstore.newCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.databinding.ActivityNewCertificationBinding;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.ApiManager2;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.newCertification.model.CertifyModel;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.LogUtil;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NewCertificationActivity extends BaseActivity {

    private ActivityNewCertificationBinding dataBinding;

    @Override
    public int setLayout() {
        return R.layout.activity_new_certification;
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

        ApiManager2.INSTANCE.get(1, this, map, Constant.MYSELF_MATCHMAKER_LIST, new ApiManager2.OnResult<CertifyModel>() {

            @Override
            public void onFailed(@NotNull String code, @NotNull String message) {

            }

            @Override
            public void onCatch(CertifyModel data) {

            }


            @Override
            public void onSuccess(CertifyModel data) {
                if (data.getCode() == 200 && data.isSuccess()) {
                    LogUtil.INSTANCE.d(data.getMessage());
                    addData(data);
                }

            }
        });
    }

    private void addData(CertifyModel data) {
        CertifyModel.EntityBean entity = data.getEntity();
        dataBinding.tvTime.setText(entity.getApplyTime());
        dataBinding.tvSmNum.setText(entity.getSmCode());
        dataBinding.tvAccount.setText(entity.getUserName());
        dataBinding.tvDistrict.setText(entity.getAddress());
        dataBinding.tvCost.setText("￥" + entity.getMoney());
        dataBinding.tvPeople.setText(entity.getRealName());
        dataBinding.tvGender.setText(entity.getSex());
        dataBinding.tvIdNum.setText(entity.getIdNo());
        dataBinding.tvAddress.setText(entity.getCardAddress());
        dataBinding.tvValid.setText(entity.getValidityPeriodStartTime() + "至" + entity.getValidityPeriodEndTime());
        dataBinding.tvAuthority.setText(entity.getIssuingAgency());

        SmApplication.Companion.getApp().setData(DataCode.ID_FRONT, entity.getIdFront());
        SmApplication.Companion.getApp().setData(DataCode.ID_BACK, entity.getIdBack());

    }

    private void initClick() {
        dataBinding.rlID.setOnClickListener(this);
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.rlID:
                IDcardActivity.start(NewCertificationActivity.this);

                break;
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewCertificationActivity.class));
    }

    @Override
    public void reTryGetData() {
        initData();
    }
}
