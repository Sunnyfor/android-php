package com.cocosh.shmstore.newCertification.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.SendBonusResultActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.MyWalletModel
import com.cocosh.shmstore.mine.model.PayResultModel
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.mine.ui.mywallet.ReChargeActivity
import com.cocosh.shmstore.newCertification.CertifimInforPresenter
import com.cocosh.shmstore.newCertification.ConfirmlnforContrat
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SercurityDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.google.gson.Gson
import com.pingplusplus.android.Pingpp
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.certifitacation_pay_activity.*
import org.json.JSONObject

/**
 *
 * Created by cjl on 2018/2/6.
 */
class PayActivity : BaseActivity(), ConfirmlnforContrat.IView {

    //支付类型 RECHARGE (1,"充值"),PAYMENT(3,"消费"), SEND_RED_PACKET(7,"发红包") ，DEPOSIT(8,"押金")

    var isConfirm = false
    internal var presenter: ConfirmlnforContrat.IPresenter = CertifimInforPresenter(this, this)
    private var check = ""
    var number: String? = null
    var amount: String? = ""
    private var runningNumber: String? = "" //订单
    private var payOperatStatus: String? = ""//支付类型
    override fun reTryGetData() {
        requestMyWalletData(1)
    }

    override fun confirmResult(result: String) {
        val jsonObject = JSONObject(result)
        if (jsonObject.optString("code") == "200" && jsonObject.optBoolean("success")) {
            val string = jsonObject.optString("entity")
            Pingpp.createPayment(this, string)
            val gson = Gson()
            var map: Map<String, Any> = java.util.HashMap()
            map = gson.fromJson(string, map.javaClass)
            isConfirm = true
            number = map["orderNo"] as String?
            isConfirm = true
        } else {
            ToastUtil.show("订单提交失败")
        }
    }

    override fun payConfirmResult(result: BaseModel<PayResultModel>) {
        if (result.code == 200 && result.success) {
            if (result.entity?.status == 11) {
                //处理中

                return
            }
            if (result.entity?.status == 12 || result.entity?.status == 14) {
                //失败
                if (payOperatStatus == AuthenStatus.SEND_RED_PACKET.type) {
                    SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                    startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "2"))
                }
                return
            }
            if (result.entity?.status == 13) {
                //成功
                if (payOperatStatus == AuthenStatus.SEND_RED_PACKET.type) {
                    setResult(IntentCode.IS_INPUT)
                    SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                    startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "0"))
                    finish()
                }
                return
            }
        } else {
//            ToastUtil.show(result.message)
        }
    }

    override fun localPay(result: BaseModel<String>) {
        mDialog?.getResult(result)
    }

    override fun setLayout(): Int = R.layout.certifitacation_pay_activity

    override fun initView() {
        titleManager.defaultTitle("支付")
        runningNumber = intent.getStringExtra("runningNumber")
        amount = intent.getStringExtra("amount")
        payOperatStatus = intent.getStringExtra("payOperatStatus")
        requestMyWalletData(1)
        llShoumei.setOnClickListener(this)
        llWechat.setOnClickListener(this)
        llAlipay.setOnClickListener(this)
        btnSure.setOnClickListener(this)
        tvCharge.setOnClickListener(this)
        tv_money.text = ("￥$amount")
    }

    override fun onListener(view: View) {}


    override fun onClick(view: View) {
        when (view.id) {
            R.id.llShoumei ->
                if (amount?.toDouble()!! <= accountMoney?.toDouble()!!) {
                    choose("LOCAL_ACC")
                }
            R.id.llWechat -> choose("PINGPP_WX")
            R.id.llAlipay -> choose("PINGPP_ALIPAY")
            R.id.btnSure -> pay(check)
            tvCharge.id -> {
                SmApplication.getApp().activityName = this::class.java as Class<BaseActivity>?
                ReChargeActivity.start(this)
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

        if (!amount.isNullOrEmpty()) {
            if (type == "LOCAL_ACC") {
                if (UserManager.getPayPwdStatus() == true) {
                    showImputPsdDialog()
                } else {
                    //弹出设置密码弹窗
                    showEntDialog()
                }
            } else {
                presenter.getCharge(type, amount!!, payOperatStatus ?: "", runningNumber ?: "")
            }
        } else {
            ToastUtil.show("支付失败，稍后重试！")
        }
    }

    private fun choose(type: String) {
        when (type) {
            "PINGPP_WX" -> {
                wechatCheck.visibility = View.VISIBLE
                alipayCheck.visibility = View.GONE
                ivCheck.visibility = View.GONE

            }
            "PINGPP_ALIPAY" -> {
                wechatCheck.visibility = View.GONE
                ivCheck.visibility = View.GONE
                alipayCheck.visibility = View.VISIBLE
            }
            "LOCAL_ACC" -> {
                ivCheck.visibility = View.VISIBLE
                wechatCheck.visibility = View.GONE
                alipayCheck.visibility = View.GONE
            }
        }

        check = type
        btnSure.isClickable = true
        btnSure.setBackgroundColor(resources.getColor(R.color.red))
    }

    companion object {
        /**
         * runningNumber 可为null
         */
        fun start(context: Context, runningNumber: String, amount: String, payOperatStatus: String) {
            context.startActivity(Intent(context, PayActivity::class.java).putExtra("runningNumber", runningNumber).putExtra("amount", amount).putExtra("payOperatStatus", payOperatStatus))
        }
    }

    private fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().isDelete = false
            SmApplication.getApp().activityName = this@PayActivity.javaClass
            CheckPayPwdMessage.start(this@PayActivity)
        }
        dialog.show()
    }

    var mDialog: SercurityDialog<String>? = null
    private fun showImputPsdDialog() {
        mDialog = SercurityDialog(this, R.style.SercurityDialogTheme)
        mDialog?.show()
        mDialog?.setOnInputCompleteListener(object : SercurityDialog.InputCompleteListener<String> {
            override fun inputComplete(pwd: String) {
                if (payOperatStatus == "DEPOSIT") {
                    paySecurity(pwd)
                } else {
                    presenter.getLocalPay(check, amount!!, payOperatStatus ?: "", runningNumber
                            ?: "", pwd)
                }

            }

            override fun result(boolean: Boolean, data: String?) {
                mDialog?.dismiss()
                if (boolean) {
                    //成功
                    if (payOperatStatus == AuthenStatus.SEND_RED_PACKET.type) {
                        SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                        startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "0"))
                        setResult(IntentCode.IS_INPUT)
                        finish()
                    }
                } else {
                    //失败
                    if (payOperatStatus == AuthenStatus.SEND_RED_PACKET.type) {
                        SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                        startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "2"))
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requestMyWalletData(1)
        if (isConfirm) {
            presenter.getConfirmResult(number ?: "")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mDialog?.clearNum()
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
                    setResult(IntentCode.FINISH)
//                    finish()
                } else {
                    if (errorMsg == "user_cancelled") {
                        ToastUtil.show("取消支付")
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    var accountMoney: String? = null
    /**
     * 获取我的钱包 账户余额
     */
    private fun requestMyWalletData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, this, map, Constant.MY_WALLET_DATA, object : ApiManager.OnResult<BaseModel<MyWalletModel>>() {
            override fun onSuccess(data: BaseModel<MyWalletModel>) {
                if (data.code == 200 && data.success) {
                    hideReTryLayout()
                    accountMoney = data.entity?.balance
                    tvCount.text = "账户余额: ￥" + data.entity?.balance
                    if (amount?.toDouble()!! > accountMoney?.toDouble()!!) {
                        ivCheck.visibility = View.GONE
                        tvCount.setTextColor(resources.getColor(R.color.red))
                        tvCharge.visibility = View.VISIBLE
                    } else {
                        tvCharge.visibility = View.GONE
                        tvCount.setTextColor(resources.getColor(R.color.blackText))
                    }
                } else {
                    showReTryLayout()
                }
            }

            override fun onFailed(e: Throwable) {
                com.cocosh.shmstore.utils.LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<MyWalletModel>) {
                com.cocosh.shmstore.utils.LogUtil.d(data.toString())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SmApplication.getApp().activityName = null
        SmApplication.getApp().isDelete = false
        SmApplication.getApp().deleteActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
    }

    fun paySecurity(pass: String) {
        val params = HashMap<String, String>()
        params["pass"] = pass
        ApiManager.post(this, params, Constant.PAY_SECURITY, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                mDialog?.hideLoading()
                mDialog?.dismiss()
                if (data.success) {
                    AuthActivity.start(this@PayActivity)
                    SuccessActivity.start(this@PayActivity)
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                mDialog?.hideLoading()
                mDialog?.dismiss()
            }

            override fun onCatch(data: BaseModel<Boolean>) {
            }
        })

    }
}