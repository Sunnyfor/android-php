package com.cocosh.shmstore.baiduFace.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.model.IDCardResult;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.application.SmApplication;
import com.cocosh.shmstore.baiduFace.APIService;
import com.cocosh.shmstore.baiduFace.Config;
import com.cocosh.shmstore.baiduFace.OnResultListener;
import com.cocosh.shmstore.baiduFace.exception.FaceException;
import com.cocosh.shmstore.baiduFace.model.AccessToken;
import com.cocosh.shmstore.baiduFace.model.LivenessVsIdcardResult;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.newCertification.contrat.FaceContrat;
import com.cocosh.shmstore.newCertification.presenter.FacePresenter;
import com.cocosh.shmstore.newCertification.ui.CertificationAddressActivity;
import com.cocosh.shmstore.utils.DataCode;
import com.cocosh.shmstore.utils.LogUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;

public class FaceLivenessExpActivity extends BaseActivity implements View.OnClickListener, FaceContrat.IView {

    private boolean waitAccesstoken = true;
    private TextView resultTipTV;
    private TextView onlineFacelivenessTipTV;
    private TextView scoreTV;
    private ImageView avatarIv;
    private Button retBtn;
    private Button btNext;
    private String filePath;
    public static final int OFFLINE_FACE_LIVENESS_REQUEST = 100;
    private FacePresenter facePresenter = new FacePresenter(FaceLivenessExpActivity.this, FaceLivenessExpActivity.this);
    private boolean isOk;

    // 在线活体检测和公安核实需要使用该token，为了防止ak、sk泄露，建议在线活体检测和公安接口在您的服务端请求
    private void initAccessToken() {

        displayTip(resultTipTV, "加载中");
        APIService.getInstance().init(getApplicationContext());
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                System.out.println("-------------" + result);
                if (result != null && !TextUtils.isEmpty(result.getAccessToken())) {
                    waitAccesstoken = false;
                    policeVerify(filePath);
                } else if (result != null) {
                    displayTip(resultTipTV, "在线活体token获取失败");
                    retBtn.setVisibility(View.VISIBLE);
                    btNext.setVisibility(View.VISIBLE);
                } else {
                    displayTip(resultTipTV, "在线活体token获取失败");
                    retBtn.setVisibility(View.VISIBLE);
                    btNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(FaceException error) {
                // TODO 错误处理
                displayTip(resultTipTV, "在线活体token获取失败");
                retBtn.setVisibility(View.VISIBLE);
                btNext.setVisibility(View.VISIBLE);
            }
        }, Config.apiKey, Config.secretKey);
    }

    /**
     * 公安接口合并在线活体，调用公安验证接口进行最后的核身比对；公安权限需要在官网控制台提交工单开启
     * 接口地址：https://aip.baidubce.com/rest/2.0/face/v2/person/verify
     * 入参为「姓名」「身份证号」「bestimage」
     * ext_fields 扩展功能。如 faceliveness 表示返回活体值, qualities 表示返回质检测结果
     * quality string 判断质 是否达标。“use” 表示做质 控制,质  好的照 会 直接拒绝
     * faceliveness string 判断活体值是否达标。 use 表示做活体控制,低于活体阈值的 照 会直接拒绝
     * quality_conf和faceliveness_conf 用于指定阈值，超过此分数才调用公安验证，
     *
     * @param filePath
     */
    private void policeVerify(String filePath) {
        if (TextUtils.isEmpty(filePath) || waitAccesstoken) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        IDCardResult front = SmApplication.Companion.getApp().getData("front", false);
        String name = front.getName().toString();
        String idNumber = front.getIdNumber().toString();

//        displayTip(resultTipTV, "公安身份核实中...");
        APIService.getInstance().policeVerify(name, idNumber, filePath, new
                OnResultListener<LivenessVsIdcardResult>() {
                    @Override
                    public void onResult(LivenessVsIdcardResult result) {
                        if (result != null && result.getScore() > 0.8) {

                        } else {

                        }
                    }

                    @Override
                    public void onError(FaceException error) {
                        delete();
                        displayTip(resultTipTV, "核身失败：" + error.getErrorMessage());
                        Toast.makeText(FaceLivenessExpActivity.this, "公安身份核实失败:" + error.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        retBtn.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 检测完成后 开始在线活体检测和公安核实
        if (requestCode == OFFLINE_FACE_LIVENESS_REQUEST && data != null) {
            filePath = data.getStringExtra("bestimage_path");
            if (TextUtils.isEmpty(filePath)) {
                Toast.makeText(this, "离线活体图片没找到", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            avatarIv.setImageBitmap(bitmap);
            policeVerify(filePath);
        } else {
            finish();
        }
    }

    private void displayTip(final TextView textView, final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setText(tip);
                }
            }
        });
    }

    private void delete() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_face_online_check;
    }

    @Override
    public void initView() {
        resultTipTV = (TextView) findViewById(R.id.result_tip_tv);
        onlineFacelivenessTipTV = (TextView) findViewById(R.id.online_faceliveness_tip_tv);
        scoreTV = (TextView) findViewById(R.id.score_tv);
        avatarIv = (ImageView) findViewById(R.id.avatar_iv);
        retBtn = (Button) findViewById(R.id.retry_btn);
        btNext = (Button) findViewById(R.id.btNext);
        retBtn.setOnClickListener(this);
        btNext.setOnClickListener(this);

        initAccessToken();
        // 打开离线活体检测

        Intent faceLivenessintent = new Intent(this, OfflineFaceLivenessActivity.class);
        startActivityForResult(faceLivenessintent, OFFLINE_FACE_LIVENESS_REQUEST);
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.retry_btn:
                if (TextUtils.isEmpty(filePath)) {
                    finish();
                    return;
                }
                if (TextUtils.isEmpty(APIService.getInstance().getAccessToken())) {
                    initAccessToken();
                } else {
                    policeVerify(filePath);
                }
                break;
            case R.id.btNext:
                showLoading();
                //向七牛发送活体图片
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UploadManager uploadManager = new UploadManager();
                        final SmApplication app = SmApplication.Companion.getApp();
                        IDCardResult front = app.getData("front", false);
                        File file = app.getData("huo", false);
                        System.out.println("-------length------" + file.length() + "----path--:" + file.getAbsolutePath());

                        String key = front.getIdNumber().toString() + "huo";//活体图片

                        //获取token
                        final String qiniuToken = SmApplication.Companion.getApp().getData(DataCode.QINIU_TOKEN, false);

                        uploadManager.put(file, key, qiniuToken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {

                                System.out.println("-------------------------------huoti");
                                LogUtil.INSTANCE.d("key:" + key + "\r\n" + info + "\r\n" + response);

                                if (info.isOK()) {
                                    LogUtil.INSTANCE.d("qiniuHuo--success");

                                } else {
                                    if (info.statusCode == ResponseInfo.InvalidToken) {
                                        SmApplication.Companion.getApp().removeData(DataCode.QINIU_TOKEN);
                                    }
                                    LogUtil.INSTANCE.d("qiniuHuo--fail");
                                }

                                app.setData(DataCode.FACE_KEY, key);
                                //删掉活体图片
                                delete();
                                CertificationAddressActivity.Companion.start(FaceLivenessExpActivity.this);
                                facePresenter.faceRequest();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                    }
                                });

                            }
                        }, null);

                    }
                }).start();
                //向服务器发送请求
//                ApiManager.INSTANCE.post(FaceLivenessExpActivity.this,);

                break;
        }
    }

    @Override
    public void faceResult(BaseModel result) {

    }

    @Override
    public void reTryGetData() {

    }
}
