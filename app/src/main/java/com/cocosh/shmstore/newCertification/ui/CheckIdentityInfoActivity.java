package com.cocosh.shmstore.newCertification.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.Word;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.baiduFace.ui.FaceMainActivity;
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.databinding.ActivityCheckIdentityInfoBinding;
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseActiveActivity;
import com.cocosh.shmstore.enterpriseCertification.ui.UpLoadAgreementActivity;
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.ApiManager2;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.mine.model.Ethnic;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.DialogHelper;
import com.cocosh.shmstore.utils.GetJsonDataUtil;
import com.cocosh.shmstore.utils.IDCard;
import com.cocosh.shmstore.utils.LogUtil;
import com.cocosh.shmstore.utils.PickerViewUtils;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.widget.dialog.BottomDialog;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.commonsdk.debug.E;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CheckIdentityInfoActivity extends BaseActivity {
    private ActivityCheckIdentityInfoBinding dataBinding;
    private ArrayList<String> sexList = new ArrayList<>();
    private String type;
    private boolean isAgent = false;
    private IDCardResult front, back;
    private IDCard idCard;
    String message = "";
    private PickerViewUtils pickerViewUtils;
    @Override
    public int setLayout() {
        return R.layout.activity_check_identity_info;
    }

    @Override
    public void initView() {

        pickerViewUtils = new PickerViewUtils(this);

        type = getIntent().getStringExtra("type");
        isAgent = getIntent().getBooleanExtra("isAgent", false);
        dataBinding = getDataBinding();
        titleManager.defaultTitle("请核对" + type + "身份信息");
        initClick();
        initData();
        setFinishOnTouchOutside(true);
        idCard = new IDCard();
    }

    /**
     * 初始化List
     */
    private void initData() {
        sexList.add("男");
        sexList.add("女");

        //获取身份证扫描结果
        if (isAgent && "代办人".equals(type)) {
            front = SmApplication.Companion.getApp().getData(DataCode.AGENT_FRONT_DATA, false);
            back = SmApplication.Companion.getApp().getData(DataCode.AGENT_BACK_DATA, false);
        } else {
            front = SmApplication.Companion.getApp().getData(IDCardParams.ID_CARD_SIDE_FRONT, false);
            back = SmApplication.Companion.getApp().getData(IDCardParams.ID_CARD_SIDE_BACK, false);
        }


        if (front != null) {
            Word name = front.getName();
            Word gender = front.getGender();
            Word address = front.getAddress();
            Word birthday = front.getBirthday();
            Word ethnic = front.getEthnic();
            Word idNumber = front.getIdNumber();

            dataBinding.etName.setText(name == null ? "" : name.toString());
            dataBinding.etSex.setText(gender == null ? "" : gender.toString());
            dataBinding.etAddress.setText(address == null ? "" : address.toString());
            dataBinding.etBirth.setText(birthday == null ? "" :
                    birthday.toString().substring(0, 4) + "-"
                            + birthday.toString().substring(4, 6) + "-"
                            + birthday.toString().substring(6));
            dataBinding.etNation.setText(ethnic == null ? "" : ethnic.toString());
            dataBinding.etIdCard.setText(idNumber == null ? "" : idNumber.toString());
        }

        if (back != null) {
            Word issueAuthority = back.getIssueAuthority();
            Word signDate = back.getSignDate();
            Word expiryDate = back.getExpiryDate();

            dataBinding.etOrganization.setText(issueAuthority == null ? "" : issueAuthority.toString());
            if (signDate.toString().length() != 8) {
                dataBinding.etDateStart.setText("");
            } else {
                dataBinding.etDateStart.setText((signDate.toString().substring(0, 4) + "-"
                        + signDate.toString().substring(4, 6) + "-"
                        + signDate.toString().substring(6)));
            }

            if (expiryDate.toString().length() != 8) {
                dataBinding.etDateEnd.setText("");
            } else {
                dataBinding.etDateEnd.setText((expiryDate.toString().substring(0, 4) + "-"
                        + expiryDate.toString().substring(4, 6) + "-"
                        + expiryDate.toString().substring(6)));
            }

        }
    }

    private void initClick() {
        dataBinding.rlSex.setOnClickListener(this);
        dataBinding.rlNation.setOnClickListener(this);
        dataBinding.itvBirth.setOnClickListener(this);
        dataBinding.itvDateStart.setOnClickListener(this);
        dataBinding.itvDateEnd.setOnClickListener(this);
        dataBinding.tvNext.setOnClickListener(this);
        dataBinding.tvAgain.setOnClickListener(this);

    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {

            case R.id.rlSex:
                final BottomDialog dialog1 = DialogHelper.showSexChooseDialog(CheckIdentityInfoActivity.this, sexList);
                dialog1.setOnCompleteListener(new BottomDialog.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        int index = dialog1.wvSex.getCurrentItem();
                        dataBinding.etSex.setText(sexList.get(index));
                    }
                });

                break;
            case R.id.rlNation:
                pickerViewUtils.showEthnic(new PickerViewUtils.OnPickerViewResultListener() {
                    @Override
                    public void onPickerViewResult(@NotNull String value) {
                        String[] result = value.split("-");
                        dataBinding.etNation.setTag(result[0]);
                        dataBinding.etNation.setText(result[1]);
                    }
                });
//                final BottomDialog dialog = DialogHelper.showNationChooseDialog(CheckIdentityInfoActivity.this, ethnicList);
//                dialog.setOnCompleteListener(new BottomDialog.OnCompleteListener() {
//
//                    @Override
//                    public void onComplete() {
//                        int index = dialog.wvSex.getCurrentItem();
//                        dataBinding.etNation.setTag(ethnicList.get(index).getId());
//                        dataBinding.etNation.setText(ethnicList.get(index).getName());
//                    }
//
//                });

                break;
            case R.id.itvBirth:
                DialogHelper.showTimePickerDialog(CheckIdentityInfoActivity.this, new DialogHelper.OnSelecteChangeListener() {
                    @Override
                    public void onSelecte(String s1) {
                        System.out.println(s1);
                        dataBinding.etBirth.setText(s1);
                    }
                });

                break;
            case R.id.itvDateStart:
                DialogHelper.showTimePickerDialog(CheckIdentityInfoActivity.this, new DialogHelper.OnSelecteChangeListener() {
                    @Override
                    public void onSelecte(String s1) {
                        System.out.println(s1);
                        dataBinding.etDateStart.setText(s1);
                    }
                });

                break;
            case R.id.itvDateEnd:
                DialogHelper.showTimePickerDialog(CheckIdentityInfoActivity.this, new DialogHelper.OnSelecteChangeListener() {
                    @Override
                    public void onSelecte(String s1) {
                        System.out.println(s1);
                        dataBinding.etDateEnd.setText(s1);
                    }
                }).setLONGLayout(true);

                break;

            case R.id.tvNext:

                if (!isOk()) {
                    ToastUtil.INSTANCE.show(message);
                    return;
                }

                updateIDCard();

                if ("实人认证".equals(type)) {
                    //活体认证
                    Intent intent = new Intent(CheckIdentityInfoActivity.this, FaceMainActivity.class);
                    startActivity(intent);
                    return;
                }
                if ("法人代表".equals(type)) {
                    if (isAgent) {
                        Intent intent = new Intent(CheckIdentityInfoActivity.this, UpLoadAgreementActivity.class);
                        startActivity(intent);
                    } else {
                        commit(); //提交用户信息
                    }
                }

                if ("代办人".equals(type)) {
                    Intent intent = new Intent(this, ScanIdCardActivity.class);
                    intent.putExtra("type", "法人代表");
                    intent.putExtra("isAgent", true);
                    startActivity(intent);
                }

                break;
            case R.id.tvAgain:
                finish();

                break;
        }

    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, CheckIdentityInfoActivity.class));
    }


    //修改身份证实体类数据为输入框确认数据
    public void updateIDCard() {
        if (front != null) {
            if (front.getIdNumber() == null) {
                Word IdNumber = new Word();
                IdNumber.setWords(dataBinding.etIdCard.getText() != null ? dataBinding.etIdCard.getText().toString() : "");
                front.setIdNumber(IdNumber);
            } else {
                front.getIdNumber().setWords(dataBinding.etIdCard.getText() != null ? dataBinding.etIdCard.getText().toString() : "");
            }

            if (front.getName() == null) {
                Word name = new Word();
                name.setWords(dataBinding.etName.getText() != null ? dataBinding.etName.getText().toString() : "");
                front.setName(name);
            } else {
                front.getName().setWords(dataBinding.etName.getText() != null ? dataBinding.etName.getText().toString() : "");
            }

            if (front.getGender() == null) {
                Word gender = new Word();
                gender.setWords(dataBinding.etSex.getText() != null ? dataBinding.etSex.getText().toString() : "");
                front.setGender(gender);

            } else {
                front.getGender().setWords(dataBinding.etSex.getText() != null ? dataBinding.etSex.getText().toString() : "");
            }

            if (front.getEthnic() == null) {
                Word ethnic = new Word();
                ethnic.setWords(dataBinding.etNation.getText() != null ? dataBinding.etNation.getText().toString() : "");
                front.setEthnic(ethnic);
            } else {
                String ethnic = (String) dataBinding.etNation.getTag();
                front.getEthnic().setWords(ethnic != null ? ethnic : "");
            }


            if (front.getBirthday() == null) {
                Word birthday = new Word();
                birthday.setWords(dataBinding.etBirth.getText() != null ? dataBinding.etBirth.getText().toString() : "");
                front.setBirthday(birthday);
            } else {
                front.getBirthday().setWords(dataBinding.etBirth.getText() != null ? dataBinding.etBirth.getText().toString() : "");
            }

            if (front.getAddress() == null) {
                Word address = new Word();
                address.setWords(dataBinding.etAddress.getText() != null ? dataBinding.etAddress.getText().toString() : "");
                front.setAddress(address);
            } else {
                front.getAddress().setWords(dataBinding.etAddress.getText() != null ? dataBinding.etAddress.getText().toString() : "");
            }
        }
        if (back != null) {
            if (back.getSignDate() == null) {
                Word signDate = new Word();
                signDate.setWords(dataBinding.etDateStart.getText() != null ? dataBinding.etDateStart.getText().toString() : "");
                back.setSignDate(signDate);
            } else {
                back.getSignDate().setWords(dataBinding.etDateStart.getText() != null ? dataBinding.etDateStart.getText().toString() : "");
            }

            if (back.getExpiryDate() == null) {
                Word expiryDate = new Word();
                expiryDate.setWords(dataBinding.etDateEnd.getText() != null ? dataBinding.etDateEnd.getText().toString() : "");
                back.setExpiryDate(expiryDate);
            } else {
                back.getExpiryDate().setWords(dataBinding.etDateEnd.getText() != null ? dataBinding.etDateEnd.getText().toString() : "");
            }

            if (back.getIssueAuthority() == null) {
                Word issueAuthority = new Word();
                issueAuthority.setWords(dataBinding.etOrganization.getText() != null ? dataBinding.etOrganization.getText().toString() : "");
                back.setIssueAuthority(issueAuthority);
            } else {
                back.getIssueAuthority().setWords(dataBinding.etOrganization.getText() != null ? dataBinding.etOrganization.getText().toString() : "");
            }
        }
    }

    //单法人认证信息提交
    public void commit() {
        Map<String, String> params = new HashMap<>();
        params.put("entrepreneurLegal.realName", front.getName().toString()); //真实姓名
        params.put("entrepreneurLegal.sex", front.getGender().toString()); //性别
        params.put("entrepreneurLegal.ethnic", front.getEthnic().toString()); //民族
        params.put("entrepreneurLegal.birth", front.getBirthday().toString()); //生日
        params.put("entrepreneurLegal.idNo", front.getIdNumber().toString()); //身份证号
        params.put("entrepreneurLegal.idFront", Constant.QINIU_KEY_HEAD + SmApplication.Companion.getApp().getData(DataCode.FRONT_URL, false));//身份证正面
        params.put("entrepreneurLegal.idBack", Constant.QINIU_KEY_HEAD + SmApplication.Companion.getApp().getData(DataCode.BACK_URL, false)); //身份证背面
        params.put("entrepreneurLegal.cardAddress", front.getAddress().toString()); //身份证地址
        params.put("entrepreneurLegal.validityPeriodStartTime", back.getSignDate().toString()); //身份证有效期开始时间
        params.put("entrepreneurLegal.validityPeriodEndTime", back.getExpiryDate().toString());//身份证有效期结束时间
        params.put("entrepreneurLegal.issuingAgency", back.getIssueAuthority().toString()); //身份证地址签发机关

        ApiManager.INSTANCE.post(this, params, Constant.SAVE_IDENTITY, new ApiManager.OnResult<BaseModel<EntActiveInfoModel>>() {
            @Override
            public void onFailed(@NotNull Throwable e) {

            }

            @Override
            public void onSuccess(BaseModel<EntActiveInfoModel> data) {
                LogUtil.INSTANCE.i("结果：" + data);
                if (data.getSuccess()) {
                    if (data.getEntity() != null) {
                        SmApplication.Companion.getApp().setData(DataCode.ACTIVE_INFO, data.getEntity());
                    }
                    startActivity(new Intent(CheckIdentityInfoActivity.this, EnterpriseActiveActivity.class));
                } else {
                    ToastUtil.INSTANCE.show(data.getMessage());
                }
            }

            @Override
            public void onCatch(BaseModel<EntActiveInfoModel> data) {

            }
        });
    }


    @Override
    public void reTryGetData() {

    }


    public boolean isOk() {

        if (dataBinding.etName.getText().length() == 0) {
            message = "请输入真实姓名";
            return false;
        }
        if (dataBinding.etSex.getText().length() == 0) {
            message = "请输入性别";
            return false;
        }
        if (dataBinding.etNation.getText().length() == 0) {
            message = "请输入民族";
            return false;
        }

        if (dataBinding.etBirth.getText().length() == 0) {
            message = "请输入生日";
            return false;
        }

        message = idCard.dDCardValidate(dataBinding.etIdCard.getText().toString());

        if (!"".equals(message)) {

            return false;
        }

        if (dataBinding.etAddress.getText().length() == 0) {
            message = "请输入身份证住址";
            return false;
        }

        if (dataBinding.etDateStart.getText().length() == 0) {
            message = "请输入身份证有效期开始时间";
            return false;
        }


        if (dataBinding.etDateEnd.getText().length() == 0) {
            message = "请输入身份证有效期结束时间";
            return false;
        }

        if (dataBinding.etOrganization.getText().length() == 0) {
            message = "请输入身份证地址签发机关";
            return false;
        }
        return true;
    }
}
