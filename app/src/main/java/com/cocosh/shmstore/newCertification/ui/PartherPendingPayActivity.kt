package com.cocosh.shmstore.newCertification.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.model.PendingPay
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_parther_pending_pay.*

/**
 *
 * 待支付页面
 * Created by zhangye on 2018/4/3.
 */
class PartherPendingPayActivity : BaseActivity() {
    override fun reTryGetData() {
        initData()
    }

    var money = "0.00"
    var bizCode = "0"
    override fun setLayout(): Int = R.layout.activity_parther_pending_pay

    override fun initView() {
        titleManager.defaultTitle("新媒人认证")
        tvPay.setOnClickListener(this)
        initData()
    }

    private fun initData() {

        ApiManager2.get(1, this, null, Constant.NEW_CERT_RESULT, object : ApiManager2.OnResult<BaseBean<PendingPay>>() {
            override fun onSuccess(data: BaseBean<PendingPay>) {
                data.message?.let {
                    tv_money.text = ("支付金额：￥${it.cert?.fee}")
                    isv_personName.setNoIconValue(it.cert?.name)
                    isv_idcard.setNoIconValue(it.cert?.idno)
                    isv_partherName.setNoIconValue(it.svc.name)
                    isv_partherAddress.setNoIconValue(it.svc.province + "-" + it.svc.city)
                    isv_person.setNoIconValue(it.svc.legal)
                    money = it.cert?.fee ?: "0.00"
                    bizCode = it.newid
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<PendingPay>) {
            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            tvPay.id -> {
                if (money == "0.00") {
                    ToastUtil.show("网络错误，请稍后重试！")
                    return
                }
                val intent = Intent(this, PayActivity::class.java)
                intent.putExtra("amount", money)
                intent.putExtra("runningNumber", bizCode)
                intent.putExtra("payOperatStatus", "1")
                startActivityForResult(intent, IntentCode.FINISH)
            }
        }
    }
}