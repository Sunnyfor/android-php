package com.cocosh.shmstore.newCertification.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
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
        ApiManager.get(1, this, hashMapOf(), Constant.GETINFOR, object : ApiManager.OnResult<BaseModel<PendingPay>>() {
            override fun onCatch(data: BaseModel<PendingPay>) {

            }

            override fun onFailed(e: Throwable) {
                ToastUtil.show(e.message)
            }

            override fun onSuccess(data: BaseModel<PendingPay>) {
                if (data.code == 200 && data.success) {
                    data.entity?.let {
                        money = it.money?:""
                        tv_money.text = ("支付金额：￥${it.money}")
                        bizCode = it.bizCode?:""
                        isv_personName.setNoIconValue(it.realName)
                        isv_idcard.setNoIconValue(it.idNo)
                        isv_partherName.setNoIconValue(it.operatorName)
                        isv_partherAddress.setNoIconValue(it.operatorArea)
                        isv_person.setNoIconValue(it.operatorLegalName)
                    }
                }
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
                intent.putExtra("payOperatStatus", "DEPOSIT")
                startActivityForResult(intent, IntentCode.FINISH)
            }
        }
    }

}