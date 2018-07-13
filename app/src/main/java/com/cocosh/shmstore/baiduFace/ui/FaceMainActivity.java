package com.cocosh.shmstore.baiduFace.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.baiduFace.Config;
import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FaceMainActivity extends BaseActivity {
    public static List<LivenessTypeEnum> livenessList = new ArrayList<LivenessTypeEnum>();
    public static boolean isLivenessRandom = false;
    public String type;

    @Override
    public int setLayout() {
        return R.layout.face_main_layout;
    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra("type");
        titleManager.defaultTitle("实人认证");
        Button btFace = (Button) findViewById(R.id.btFace);
        btFace.setOnClickListener(this);

        // 根据需求添加活体动作
        livenessList.clear();
        livenessList.add(LivenessTypeEnum.Mouth);
        livenessList.add(LivenessTypeEnum.HeadRight);
        requestPermissions(Manifest.permission.CAMERA);

        initLib();
    }

    @Override
    public void onListener(@NotNull View view) {
        switch (view.getId()) {
            case R.id.btFace:
                startItemActivity(OfflineFaceLivenessActivity.class);
                break;
        }
    }

    /**
     * 初始化SDK
     */
    private void initLib() {
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(livenessList);
        config.setLivenessRandom(isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
//        config.setLivenessRandomCount(2);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    private void startItemActivity(Class itemClass) {
        Intent it = new Intent(this, itemClass);
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
                SmediaDialog dialog = new SmediaDialog(FaceMainActivity.this);
                dialog.setTitle(data.getStringExtra("message"));
                dialog.singleButton();
                dialog.show();
                break;
        }
    }

    public void requestPermissions(String permission) {
        if (permission != null && permission.length() > 0) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    // 检查是否有权限
                    int hasPer = checkSelfPermission(permission);
                    if (hasPer != PackageManager.PERMISSION_GRANTED) {
                        // 是否应该显示权限请求
                        boolean isShould = shouldShowRequestPermissionRationale(permission);
                        requestPermissions(new String[]{permission}, 99);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean flag = false;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                flag = true;
            }
        }
        if (!flag) {
            requestPermissions(Manifest.permission.CAMERA);
        }
    }

    @Override
    public void reTryGetData() {

    }
}
