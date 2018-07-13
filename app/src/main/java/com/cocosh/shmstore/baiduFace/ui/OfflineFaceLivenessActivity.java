/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.baiduFace.APIService;
import com.cocosh.shmstore.baiduFace.Config;
import com.cocosh.shmstore.baiduFace.OnResultListener;
import com.cocosh.shmstore.baiduFace.exception.FaceException;
import com.cocosh.shmstore.baiduFace.model.AccessToken;
import com.cocosh.shmstore.baiduFace.model.LivenessVsIdcardResult;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.mine.model.AuthenStatus;
import com.cocosh.shmstore.mine.model.MemberEntrance;
import com.cocosh.shmstore.mine.ui.SetPayPwdActivity;
import com.cocosh.shmstore.mine.ui.mywallet.BankCardMangerActivity;
import com.cocosh.shmstore.person.PersonSuccessActivity;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.LogUtil;
import com.cocosh.shmstore.utils.NetworkUtils;
import com.cocosh.shmstore.utils.ToastUtil;
import com.cocosh.shmstore.utils.UserManager;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflineFaceLivenessActivity extends FaceLivenessActivity {

    private String bestImagePath;
    private boolean check;
    private Map<String, String> codeMap = new HashMap<>();

    @Override
    public void initView() {
        super.initView();
        titleManager.goneTitle();

        codeMap.put("id number format error", "身份证格式错误，请检查后重新输入");
        codeMap.put("id number and name not match or id number not exist", "身份证号码与姓名不匹配，或无法找到此身份证号码");
        codeMap.put("id number format error", "身份证格式错误，请检查后重新输入");
        codeMap.put("id number format error", "身份证格式错误，请检查后重新输入");

        initFaceSDK();
    }

    /**
     * 初始化SDK
     */
    private void initFaceSDK() {
        // 第一个参数 应用上下文
        // 第二个参数 licenseID license申请界面查看
        // 第三个参数 assets目录下的License文件名
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行调整。如果没有指定动作，将使用所有的动作
        List<LivenessTypeEnum> livenessList = new ArrayList<>();
        livenessList.add(LivenessTypeEnum.Mouth);
        livenessList.add(LivenessTypeEnum.HeadLeft);
        config.setLivenessTypeList(livenessList);

        // 设置 活体动作是否随机 boolean
        config.setLivenessRandom(false);
        config.setLivenessRandomCount(2);
        // 模糊度范围 (0-1) 推荐小于0.7
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 人脸置信度（0-1）推荐大于0.6
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        // 是否进行质量检测
        config.setCheckFaceQuality(true);
        // 人脸检测使用线程数
        // config.setFaceDecodeNumberOfThreads(4);
        // 是否开启提示音
        config.setSound(true);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    /**
     * 人脸检测结果
     *
     * @param status
     * @param message
     * @param base64ImageMap
     */
    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        //检测成功
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            saveImage(base64ImageMap);
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            //检测超时
            SmediaDialog dialog = new SmediaDialog(this);
            dialog.setTitle("检测超时");
            dialog.singleButton();
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            dialog.show();
        }
    }


    private void saveImage(HashMap<String, String> imageMap) {
        String bestimageBase64 = imageMap.get("bestImage0");
        Bitmap bmp = base64ToBitmap(bestimageBase64);

        try {
            File file = File.createTempFile("face", ".jpg");

            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            bestImagePath = file.getAbsolutePath();
            //保存活体图片
            updateFace(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        File file = new File(bestImagePath);
        if (file.exists()) {
            file.delete();
        }
    }


    public void requestFace() {
        if (check) {
            //公安系统验证成功
            checkFace();
        } else {
            finish();
        }
    }

    IDCardResult front;

    public void checkFace() {
        SmApplication app = SmApplication.Companion.getApp();
        front = app.getData("front", false);
        String idNumber = front.getIdNumber().toString();
        String name = front.getName().toString();
        String birthday = front.getBirthday().toString();
        String ethnic = front.getEthnic().toString();
        String gender = front.getGender().toString();
        String address = front.getAddress().toString();

        IDCardResult back = app.getData("back", false);
        final String issueAuthority = back.getIssueAuthority().toString();
        String signDate = back.getSignDate().toString();
        String expiryDate = back.getExpiryDate().toString();

        Map<String, String> map = new HashMap<>();
        map.put("realName", name);
        map.put("idNo", idNumber);
        String idFront = Constant.QINIU_KEY_HEAD + app.getData(DataCode.FRONT_URL, false);
        map.put("idFront", idFront);
        String idBack = Constant.QINIU_KEY_HEAD + app.getData(DataCode.BACK_URL, false);
        map.put("idBack", idBack);
        map.put("sex", gender);
        map.put("ethnic", ethnic);
        map.put("birth", birthday);
        map.put("cardAddress", address);
        map.put("validityPeriodStartTime", signDate);
        map.put("validityPeriodEndTime", expiryDate);
        map.put("issuingAgency", issueAuthority);
        map.put("faceRecognition", "1");
        String face = app.getData(DataCode.FACE_KEY, false);
        map.put("liveRecognitionPicture", Constant.QINIU_KEY_HEAD + face);

        ApiManager.INSTANCE.post(this, map, Constant.IDCARD_IDENTITY, new ApiManager.OnResult<BaseModel>() {

            @Override
            public void onCatch(BaseModel data) {

            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                LogUtil.INSTANCE.d(e.getMessage());
                SmediaDialog dialog = new SmediaDialog(OfflineFaceLivenessActivity.this);
                dialog.singleButton();
                dialog.setTitle("请求失败");
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                dialog.show();
            }

            @Override
            public void onSuccess(BaseModel data) {
                if (data.getCode() == 200 && data.getSuccess()) {
                    //  AuthActivity.Companion.start(OfflineFaceLivenessActivity.this);
                    UserManager.INSTANCE.setPersonStatus(AuthenStatus.PERSION_OK.getType());
                    //跳转设置密码页
                    if (SmApplication.Companion.getApp().getActivityName() != null) {
                        ToastUtil.INSTANCE.show("实人认证成功");
                        MemberEntrance memberEntrance = UserManager.INSTANCE.getMemberEntrance();
                        memberEntrance.setRealName(front.getName().toString());
                        memberEntrance.setPersonStatus(AuthenStatus.PERSION_OK.getType());
                        UserManager.INSTANCE.setMemberEntrance(memberEntrance);
                        SetPayPwdActivity.Companion.start(OfflineFaceLivenessActivity.this);
                        return;
                    }
                    PersonSuccessActivity.Companion.start(OfflineFaceLivenessActivity.this);
                    finish();//成功后跳转，关闭当前页面
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("message", data.getMessage() == null ? "未知错误" : data.getMessage());
                    setResult(2, intent);
                    finish();
                }
                LogUtil.INSTANCE.d(data.toString());
            }
        });
    }

    // 在线活体检测和公安核实需要使用该token，为了防止ak、sk泄露，建议在线活体检测和公安接口在您的服务端请求
    private void initAccessToken() {
        showLoading();
        APIService.getInstance().init(getApplicationContext());
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                hideLoading();
                System.out.println("-------------" + result);
                if (result != null && !TextUtils.isEmpty(result.getAccessToken())) {
                    startCertify();
                } else {
                    SmediaDialog dialog = new SmediaDialog(OfflineFaceLivenessActivity.this);
                    dialog.setTitle("获取验证信息失败");
                    dialog.singleButton();
                    dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onError(FaceException error) {
                // TODO 错误处理
                hideLoading();
                SmediaDialog dialog = new SmediaDialog(OfflineFaceLivenessActivity.this);
                dialog.setTitle("获取验证信息错误");
                dialog.singleButton();
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                dialog.show();
            }
        }, Config.apiKey, Config.secretKey);
    }

    /**
     * 开始公安系统验证
     */
    public void startCertify() {
        IDCardResult front = SmApplication.Companion.getApp().getData("front", false);
        String name = front.getName().toString();
        String idNumber = front.getIdNumber().toString();

        if (!NetworkUtils.INSTANCE.isNetworkAvaliable(this)) {
            SmediaDialog dialog = new SmediaDialog(this);
            dialog.cancleFinish();
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCertify();
                }
            });
            dialog.showNetWorkError();
            return;
        }

        showLoading();

        final SmediaDialog faceDialog = new SmediaDialog(OfflineFaceLivenessActivity.this);
        faceDialog.singleButton();
        faceDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFace();
            }
        });

        APIService.getInstance().policeVerify(name, idNumber, bestImagePath, new
                OnResultListener<LivenessVsIdcardResult>() {
                    @Override
                    public void onResult(LivenessVsIdcardResult result) {
                        hideLoading();

                        if (result != null && result.getScore() > 0.8) {
                            check = true;
                            faceDialog.setTitle("识别通过");
                            faceDialog.setPositiveText("提交审核");
                            faceDialog.show();
                        } else {
                            check = false;
                            faceDialog.setTitle("识别失败");
                            faceDialog.cancleFinish();
                            faceDialog.show();
                        }
                    }

                    @Override
                    public void onError(FaceException error) {
                        hideLoading();
                        check = false;
                        delete();
                        faceDialog.setTitle("识别失败");
                        faceDialog.cancleFinish();
                        faceDialog.show();
                    }
                });
    }

    /**
     * 向七牛上传活体照片
     */
    public void updateFace(final File file) {
        if (!NetworkUtils.INSTANCE.isNetworkAvaliable(this)) {
            SmediaDialog dialog = new SmediaDialog(this);
            dialog.cancleFinish();
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFace(file);
                }
            });
            dialog.showNetWorkError();
            return;
        }
        //获取token
        String key = UserManager.INSTANCE.getUserId() + System.currentTimeMillis() + "face.jpg";//活体图片
        final String qiniuToken = SmApplication.Companion.getApp().getData(DataCode.QINIU_TOKEN, false);
        UploadManager uploadManager = new UploadManager();

        uploadManager.put(file, key, qiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(final String key, final ResponseInfo info, final JSONObject response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.INSTANCE.d("key:" + key + "\r\n" + info + "\r\n" + response);

                        if (info.isOK()) {
                            LogUtil.INSTANCE.d("活体图片上传成功");
                            SmApplication.Companion.getApp().setData(DataCode.FACE_KEY, key);
                            initAccessToken();
                        } else {
                            if (info.statusCode == ResponseInfo.InvalidToken) {
                                SmApplication.Companion.getApp().removeData(DataCode.QINIU_TOKEN);
                            }
                            LogUtil.INSTANCE.d("活体图片上传失败");
                            SmediaDialog dialog = new SmediaDialog(OfflineFaceLivenessActivity.this);
                            dialog.setTitle("上传失败");
                            dialog.setPositiveText("重试");
                            dialog.cancleFinish();
                            dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateFace(file);
                                }
                            });
                            dialog.show();
                        }
                    }
                });
            }
        }, null);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, OfflineFaceLivenessActivity.class));
    }
}

