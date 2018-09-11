package com.cocosh.shmstore.newCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseBean;
import com.cocosh.shmstore.databinding.ActivityNewCertificationBinding;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.ApiManager2;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.newCertification.model.CertifyModel;
import com.cocosh.shmstore.newCertification.model.PendingPay;
import com.cocosh.shmstore.person.model.PersonResult;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.LogUtil;
import com.cocosh.shmstore.utils.UserManager2;
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

        ApiManager2.INSTANCE.get(1, this, map, Constant.NEW_CERT_RESULT, new ApiManager2.OnResult<BaseBean<PersonResult>>() {

            @Override
            public void onFailed(@NotNull String code, @NotNull String message) {

            }

            @Override
            public void onCatch(BaseBean<PersonResult> data) {

            }


            @Override
            public void onSuccess(BaseBean<PersonResult> data) {
                    addData(data.getMessage());
            }
        });
    }

    private void addData(PersonResult data) {
        dataBinding.tvTime.setText(data.getCert().getTime());
        dataBinding.tvSmNum.setText(UserManager2.INSTANCE.getCommonData().getSmno());
        dataBinding.tvAccount.setText(UserManager2.INSTANCE.getCommonData().getPhone());
        dataBinding.tvDistrict.setText((data.getCert().getProvince()+"-"+ data.getCert().getCity()));
        dataBinding.tvCost.setText(("￥" + data.getCert().getFee()));
        dataBinding.tvPeople.setText(data.getBase().getName());
        if (data.getBase().getGender() == 1){
            dataBinding.tvGender.setText("男");
        }else{
            dataBinding.tvGender.setText("女");
        }
        dataBinding.tvIdNum.setText(data.getBase().getIdno());
        dataBinding.tvAddress.setText(data.getBase().getAddr());
        dataBinding.tvValid.setText((data.getBase().getBeg_time() + "至" + data.getBase().getEnd_time()));
        dataBinding.tvAuthority.setText(data.getBase().getOrg());
    }

    private void initClick() {
        dataBinding.rlID.setOnClickListener(this);
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.rlID:
//                IDcardActivity.start(NewCertificationActivity.this);
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
