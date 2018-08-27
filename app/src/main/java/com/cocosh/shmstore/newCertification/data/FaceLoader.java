package com.cocosh.shmstore.newCertification.data;

import com.baidu.ocr.sdk.model.IDCardResult;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.http.ApiManager;
import com.cocosh.shmstore.http.Constant;
import com.cocosh.shmstore.newCertification.contrat.FaceContrat;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wt on 2018/2/8.
 */

public class FaceLoader {
    private BaseActivity activity;
    private FaceContrat.IView faceView;

    public FaceLoader(BaseActivity activity, FaceContrat.IView faceView) {
        this.activity = activity;
        this.faceView = faceView;
    }

    public void requestFace() {
        SmApplication app = SmApplication.Companion.getApp();
        IDCardResult front = app.getData("front", false);
        String idNumber = front.getIdNumber().toString();
        String name = front.getName().toString();
        String birthday = front.getBirthday().toString();
        String ethnic = front.getEthnic().toString();
        String gender = front.getGender().toString();
        String address = front.getAddress().toString();

        IDCardResult back = app.getData("back", false);
        String issueAuthority = back.getIssueAuthority().toString();
        String signDate = back.getSignDate().toString();
        String expiryDate = back.getExpiryDate().toString();

        Map<String, String> map = new HashMap<>();
        map.put("nickname", name);
        map.put("idNo", idNumber);
        String idFront = app.getData(DataCode.FRONT_URL, false).toString();
        map.put("idFront", idFront);
        String idBack = app.getData(DataCode.BACK_URL, false).toString();
        map.put("idBack", idBack);
        map.put("sex", gender);
        map.put("ethnic", ethnic);
        map.put("birth", birthday);
        map.put("cardAddress", address);
        map.put("validityPeriodStartTime", signDate);
        map.put("validityPeriodEndTime", expiryDate);
        map.put("issuingAgency", issueAuthority);
        map.put("type", "C");
        map.put("faceRecognition", "1");
        String huoti = app.getData(DataCode.FACE_KEY, false).toString();
        map.put("liveRecognitionPicture", huoti);

        ApiManager.INSTANCE.post(activity, map, Constant.IDCARD_IDENTITY, new ApiManager.OnResult<BaseModel>() {

            @Override
            public void onCatch(BaseModel data) {

            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                LogUtil.INSTANCE.d(e.getMessage());

            }

            @Override
            public void onSuccess(BaseModel data) {
                System.out.println("-------------");
                LogUtil.INSTANCE.d(data.toString());
            }
        });

    }

    /*public void tokenRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("dataType", "1");
        ApiManager.INSTANCE.post(activity, map, Constant.FACE_TOKEN, new ApiManager.OnResult<String>() {

            @Override
            public void onCatch(String data) {

            }

            @Override
            public void onFailed(@NotNull Throwable e) {
                System.out.println("获取token失败");
                LogUtil.INSTANCE.d(e.getMessage());
            }

            @Override
            public void onSuccess(String data) {
                System.out.println("--------" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    faceView.tokenResult(jsonObject.optString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}
