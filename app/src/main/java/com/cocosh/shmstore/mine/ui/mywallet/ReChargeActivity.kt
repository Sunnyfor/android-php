package com.cocosh.shmstore.mine.ui.mywallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.PayResultModel
import com.cocosh.shmstore.newCertification.CertifimInforPresenter
import com.cocosh.shmstore.newCertification.ConfirmlnforContrat
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.CashierInputFilter
import com.cocosh.shmstore.utils.OpenType
import com.cocosh.shmstore.utils.ToastUtil
import com.google.gson.Gson
import com.pingplusplus.android.Pingpp
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_recharge.*
import org.json.JSONObject
import java.util.*

/**
 * Created by lmg on 2018/4/18.
 */
class ReChargeActivity : BaseActivity(), ConfirmlnforContrat.IView {
    var isConfirm = false
    var isClick = false
    private var check = ""
    var channl: String? = null
    var time: String? = null
    var number: String? = null
    var presenter = CertifimInforPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_recharge

    override fun confirmResult(result: String) {
        val jsonObject = JSONObject(result)
        if (jsonObject.optString("code") == "200" && jsonObject.optBoolean("success")) {
            val string = jsonObject.optString("entity")
            Pingpp.createPayment(this, string)
            val gson = Gson()
            var map: Map<String, Any> = HashMap()
            map = gson.fromJson(string, map.javaClass)
            isConfirm = true
            number = map["orderNo"] as String?
        } else {
            ToastUtil.show("网络异常！")
        }
    }

    override fun payConfirmResult(result: BaseBean<PayResultModel>) {
//        if (result.code == 200 && result.success) {
//            if (result.entity?.status == 11) {
//                //处理中
////                ReChargeResult.start(this, 1)
////                ToastUtil.show("处理中")
//                return
//            }
//            if (result.entity?.status == 12 || result.entity?.status == 14) {
//                //失败
//                ReChargeResult.start(this, 2)
//                return
//            }
//            if (result.entity?.status == 13) {
//                //成功
//                ReChargeResult.start(this, 3, result.entity?.money ?: "", result.entity?.time
//                        ?: "", result.entity?.catogry ?: "", result.entity?.runningNumber ?: "")
//                return
//            }
//        } else {
////            ToastUtil.show(result.message)
//        }
    }

    override fun localPay(result: BaseBean<String>) {

    }

    override fun initView() {
        titleManager.defaultTitle("充值")

        val mFilters = arrayOf<InputFilter>(CashierInputFilter())
        tvMoney.filters = mFilters

        text_treaty2.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        text_treaty2.paint.color = resources.getColor(R.color.red)
        text_treaty2.paint.isAntiAlias = true//抗锯齿

        btnCharge.setBackgroundResource(R.color.grayBtn)
        text_treaty2.setOnClickListener(this)
        btnCharge.setOnClickListener(this)
        winxinRl.setOnClickListener(this)
        aliPay.setOnClickListener(this)
        check = "PINGPP_WX"
        tvMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvMoney.text.isEmpty()) {
                    hint.visibility = View.VISIBLE
                    isClick = false
                    btnCharge.setBackgroundResource(R.color.grayBtn)
                } else {
                    hint.visibility = View.INVISIBLE
                    if (tvMoney.text.toString().toDouble() > 0) {
                        isClick = true
                        btnCharge.setBackgroundResource(R.color.red)
                    }
                }
            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            text_treaty2.id -> {
                startActivity(Intent(this, ServiceTermActivity::class.java).putExtra("OPEN_TYPE", OpenType.Charge.name))
            }
            winxinRl.id -> choose("PINGPP_WX")
            aliPay.id -> choose("PINGPP_ALIPAY")

            btnCharge.id -> {
                if (isClick) {
//                    ReChargeResult.start(this)
                    pay(check)
                }
            }
        }
    }

    private fun pay(type: String) {
        if (type.isEmpty()) {
            ToastUtil.show("请选择支付方式")
            return
        }
        if (type == "PINGPP_WX") {
            if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                ToastUtil.show(getString(R.string.notWeixin))
                return
            }
        }

        if (!tvMoney.text.isNullOrEmpty()) {
//            presenter.getCharge(type, tvMoney.text.toString(), AuthenStatus.RECHARGE.type, "")
        } else {
            ToastUtil.show("支付失败，稍后重试！")
        }
    }

    private fun choose(type: String) {
        when (type) {
            "PINGPP_WX" -> {
                weixinImage.visibility = View.VISIBLE
                aliImage.visibility = View.GONE
            }
            "PINGPP_ALIPAY" -> {
                weixinImage.visibility = View.GONE
                aliImage.visibility = View.VISIBLE
            }
        }
        check = type
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.extras?.getString("pay_result")
                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                val errorMsg = data?.extras?.getString("error_msg") // 错误信息
                val extraMsg = data?.extras?.getString("extra_msg") // 错误信息

                if (TextUtils.equals(result, "success")) {
//                    ReChargeResult.start(this)
                } else {
                    if (errorMsg == "user_cancelled") {
                        ToastUtil.show("取消支付")
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        if (isConfirm) {
            presenter.getConfirmResult(number ?: "")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tvMoney.text = null
        isConfirm = false
    }

    override fun reTryGetData() {
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, ReChargeActivity::class.java))
        }
    }
}