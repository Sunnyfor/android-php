package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
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
 *
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
        if (jsonObject.optString("status") == "200") {
            val string = jsonObject.optString("message")
            Pingpp.createPayment(this, string)
            val gson = Gson()
            var map: Map<String, Any> = HashMap()
            map = gson.fromJson(string, map.javaClass)
            isConfirm = true
            number = map["order_no"] as String?
        } else {
            ToastUtil.show("网络异常！")
        }
    }

    override fun payConfirmResult(result: BaseBean<PayResultModel>) {
        result.message?.let {
            isConfirm = false
            when (it.status) {
                "1" -> ReChargeResult.start(this, 3, it.detail?.amount ?: "", it.detail?.time
                        ?: "", it.detail?.pay_type ?: "", it.detail?.flowno ?: "")
                else -> ReChargeResult.start(this, 2)
            }
        }
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
        check = "wx"
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
                    }else{
                        isClick = false
                        btnCharge.setBackgroundResource(R.color.grayBtn)
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
            winxinRl.id -> choose("wx")
            aliPay.id -> choose("alipay")

            btnCharge.id -> {
                if (isClick) {
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
        if (type == "wx") {
            if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                ToastUtil.show(getString(R.string.notWeixin))
                return
            }
        }

        if (!tvMoney.text.isNullOrEmpty()) {
            presenter.getCharge(type, tvMoney.text.toString(), "3", "")
        } else {
            ToastUtil.show("支付失败，稍后重试！")
        }
    }

    private fun choose(type: String) {
        when (type) {
            "wx" -> {
                weixinImage.visibility = View.VISIBLE
                aliImage.visibility = View.GONE
            }
            "alipay" -> {
                weixinImage.visibility = View.GONE
                aliImage.visibility = View.VISIBLE
            }
        }
        check = type
    }


    override fun onResume() {
        super.onResume()
        if (isConfirm) {
            presenter.getConfirmResult(number ?: "", "3")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tvMoney.text = null
        isConfirm = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            val result = data.extras.getString("pay_result")
            /* 处理返回值
        * "success" - 支付成功
        * "fail"    - 支付失败
        * "cancel"  - 取消支付
        * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
        * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
        */
            if (result == "cancel"){
                isConfirm = false
                ToastUtil.show("取消支付")
            }
        }
    }


    override fun reTryGetData() {
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, ReChargeActivity::class.java))
        }
    }
}