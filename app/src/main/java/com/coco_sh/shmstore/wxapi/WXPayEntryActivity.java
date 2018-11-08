package com.coco_sh.shmstore.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;


import com.cocosh.shmstore.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Desc:    微信支付 回调
 * Author: chencha
 * Date: 16/11/15
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mApi = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = WXAPIFactory.createWXAPI(this, "wx19de5d8fad765de7", false);
        mApi.registerApp("wx19de5d8fad765de7");
        mApi.handleIntent(getIntent(), this);
    }


    //微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }


    //第三方应用发送到微信的请求处理后的响应结果，会回调该方法
    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            String code = String.valueOf(resp.errCode);
            if (code.equals("0")) {
                Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                WXPayEntryActivity.this.finish();
            } else if (code.equals("-1")) {
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                WXPayEntryActivity.this.finish();

            } else if (code.equals("-2")) {
                Toast.makeText(WXPayEntryActivity.this, "用户取消支付", Toast.LENGTH_SHORT).show();
                WXPayEntryActivity.this.finish();
            }
        }
    }


}
