package com.cocosh.shmstore.newCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.SendBonusResultActivity
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.PayResultModel
import com.cocosh.shmstore.mine.model.WalletModel
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.mine.ui.mywallet.ReChargeActivity
import com.cocosh.shmstore.newCertification.CertifimInforPresenter
import com.cocosh.shmstore.newCertification.ConfirmlnforContrat
import com.cocosh.shmstore.sms.type.SMSType
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
        if (jsonObject.optString("status") == "200") {
            val string = jsonObject.optString("message")
            Pingpp.createPayment(this, string)
            val gson = Gson()
            var map: Map<String, Any> = java.util.HashMap()
            map = gson.fromJson(string, map.javaClass)
            isConfirm = true
            number = map["order_no"] as String?
            isConfirm = true
        } else {
            ToastUtil.show("订单提交失败")
        }
    }

    override fun payConfirmResult(result: BaseBean<PayResultModel>) {
        if (payOperatStatus == "1") {
            if (result.message?.status == "2") {
                AuthActivity.start(this@PayActivity)
                SuccessActivity.start(this@PayActivity)
            }
        }

        if (payOperatStatus == "2") {
            if (result.message?.status == "2") {
                setResult(IntentCode.IS_INPUT)
                SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "0"))
                finish()
            } else {
                SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "2"))
            }
        }

    }

    override fun localPay(result: BaseBean<String>) {
        if (result.status == "200") {
            mDialog?.getResult(result)
        } else {
            mDialog?.dismiss()
        }
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
        tv_money.text = ("￥${StringUtils.insertComma(amount?.toFloat() ?: 0f)}")
    }

    override fun onListener(view: View) {}


    override fun onClick(view: View) {
        when (view.id) {
            R.id.llShoumei ->
                if ((amount ?: "0").toDouble() <= (accountMoney ?: "0").toDouble()) {
                    choose("LOCAL_ACC")
                }
            R.id.llWechat -> choose("wx")
            R.id.llAlipay -> choose("alipay")
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
        if (type == "wx") {
            if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                ToastUtil.show(getString(R.string.notWeixin))
                return
            }
        }

        if (!amount.isNullOrEmpty()) {
            if (type == "LOCAL_ACC") {
                if (UserManager2.getCommonData()?.paypass == 1) {
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
            "wx" -> {
                wechatCheck.visibility = View.VISIBLE
                alipayCheck.visibility = View.GONE
                ivCheck.visibility = View.GONE

            }
            "alipay" -> {
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
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager2.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().isDelete = false
            SmApplication.getApp().activityName = this@PayActivity.javaClass
            CheckPayPwdMessage.start(this@PayActivity, SMSType.INIT_PAYPASS)
        }
        dialog.show()
    }

    var mDialog: SercurityDialog<String>? = null
    private fun showImputPsdDialog() {
        mDialog = SercurityDialog(this, R.style.SercurityDialogTheme)
        mDialog?.show()
        mDialog?.setOnInputCompleteListener(object : SercurityDialog.InputCompleteListener<String> {
            override fun inputComplete(pwd: String) {
                presenter.getLocalPay(amount ?: "0", payOperatStatus ?: "", runningNumber
                        ?: "", pwd)
            }

            override fun result(boolean: Boolean, data: String?) {
                mDialog?.dismiss()
                if (boolean) {
                    //成功
                    if (payOperatStatus == "1") {
                        AuthActivity.start(this@PayActivity)
                        SuccessActivity.start(this@PayActivity)
                    }

                    finish()

                    //红包类型
                    if (payOperatStatus == "2") {
                        SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this@PayActivity)
                        startActivity(Intent(this@PayActivity, SendBonusResultActivity::class.java).putExtra("type", "0"))
                        setResult(IntentCode.IS_INPUT)
                    }
                } else {
                    //失败
                    if (payOperatStatus == "2") {
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
            presenter.getConfirmResult(number ?: "", payOperatStatus ?: "")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mDialog?.clearNum()
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
            if (result == "cancel") {
                isConfirm = false
                ToastUtil.show("取消支付")
            }
        }
    }

    var accountMoney: String? = null
    /**
     * 获取我的钱包 账户余额
     */
    private fun requestMyWalletData(flag: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "p"
        ApiManager2.post(flag, this, map, Constant.EWT, object : ApiManager2.OnResult<BaseBean<WalletModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<WalletModel>) {
                hideReTryLayout()
                accountMoney = data.message?.p?.balance?.total
                tvCount.text = ("账户余额: ￥" + accountMoney)
                if ((amount ?: "0").toDouble() > (accountMoney ?: "0").toDouble()) {
                    ivCheck.visibility = View.GONE
                    tvCount.setTextColor(resources.getColor(R.color.red))
                    tvCharge.visibility = View.VISIBLE
                } else {
                    tvCharge.visibility = View.GONE
                    tvCount.setTextColor(resources.getColor(R.color.blackText))
                }
            }

            override fun onCatch(data: BaseBean<WalletModel>) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SmApplication.getApp().activityName = null
        SmApplication.getApp().isDelete = false
        SmApplication.getApp().deleteActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
    }
}