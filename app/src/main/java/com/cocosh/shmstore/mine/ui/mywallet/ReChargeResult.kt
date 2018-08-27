package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.StringUtils
import kotlinx.android.synthetic.main.activity_recharge_result.*

/**
 * Created by lmg on 2018/4/18.
 */
class ReChargeResult : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_recharge_result

    override fun initView() {
        var typeInfo = intent.getIntExtra("TYPE", -1)
        titleManager.rightText("充值", "完成", View.OnClickListener {
            if (typeInfo == 3) {
                if (SmApplication.getApp().activityName != null) {
                    startActivity(Intent(this, SmApplication.getApp().activityName))
                    finish()
                    return@OnClickListener
                }

                MyWalletActivity.start(this@ReChargeResult)
                finish()
                return@OnClickListener
            }
            ReChargeActivity.start(this@ReChargeResult)
        }, true)


        if (typeInfo == 1) {
            //处理中
            icon.setImageResource(R.drawable.pay_result_loading)
            tvResult.text = "充值中，稍后短信通知"
        } else if (typeInfo == 3) {
            //成功
            icon.setImageResource(R.drawable.success_s)
            tvResult.text = "充值成功"
            money.setName("充值金额")
            time.setName("充值时间")
            type.setName("支付方式")
            number.setName("流水号")

            money.visibility = View.VISIBLE
            time.visibility = View.VISIBLE
            type.visibility = View.VISIBLE
            number.visibility = View.VISIBLE
            money.setNoIconValue(StringUtils.insertComma(intent.getStringExtra("profit")
                    ?: "0", 2) + "元")
            time.setNoIconValue(intent.getStringExtra("time") ?: "")
            var typeWay = intent.getStringExtra("typeWay") ?: ""
            if ("PINGPP_WX" == typeWay) {
                type.setNoIconValue("微信支付")
            } else {
                type.setNoIconValue("支付宝支付")
            }
            number.setNoIconValue(intent.getStringExtra("number") ?: "")
        } else {
            //失败
            icon.setImageResource(R.drawable.error_fail)
            tvResult.text = "充值失败"
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    companion object {
        fun start(mContext: Context, type: Int) {
            mContext.startActivity(Intent(mContext, ReChargeResult::class.java).putExtra("TYPE", type))
        }

        fun start(mContext: Context, type: Int, money: String, time: String, typeWay: String, number: String) {
            mContext.startActivity(Intent(mContext, ReChargeResult::class.java).putExtra("TYPE", type)
                    .putExtra("profit", money)
                    .putExtra("time", time)
                    .putExtra("typeWay", typeWay)
                    .putExtra("number", number))
        }
    }

    override fun onBackPressed() {
        return
        super.onBackPressed()
    }
}