package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.mine.ui.authentication.IncomeActivity
import com.cocosh.shmstore.utils.StringUtils
import kotlinx.android.synthetic.main.activity_out_result.*

/**
 * Created by lmg on 2018/4/18.
 */
class OutToWalletResult : BaseActivity() {
    var moneyValue: String? = ""
    var timeValue: String? = ""
    var runningWaterValue: String? = ""
    override fun setLayout(): Int = R.layout.activity_out_result

    override fun initView() {
        val type = intent.getStringExtra("type")
        var title = ""
        if (type == CommonType.FACILITATOR_OUTTOWALLET.type) {
            title = "转出至服务商钱包"
        } else if (type == CommonType.CERTIFICATION_OUTTOWALLET.type) {
            title = "转出至我的钱包"
        } else {
            title = "转出至我的钱包"
        }
        titleManager.rightText(title, "完成", View.OnClickListener {
            if (type == CommonType.FACILITATOR_OUTTOWALLET.type) {
                IncomeActivity.start(this@OutToWalletResult, CommonType.FACILITATOR_INCOME.type)
            } else if (type == CommonType.CERTIFICATION_OUTTOWALLET.type) {
                IncomeActivity.start(this@OutToWalletResult, CommonType.CERTIFICATION_INCOME.type)
            } else {
                RedAccountActivity.start(this@OutToWalletResult)
            }
            finish()
        }, true)

        tvResult.text = "转出成功！"
        money.setName("转出金额")
        time.setName("转出时间")
        number.setName("流水号")

        moneyValue = intent.getStringExtra("profit")
        timeValue = intent.getStringExtra("time")
        runningWaterValue = intent.getStringExtra("runningWater")

        money.setValue(StringUtils.insertComma(moneyValue,2) + "元")
        time.setValue(timeValue)
        number.setValue(runningWaterValue)

        if (moneyValue != null) {
            icon.setImageResource(R.drawable.success_s)
        } else {
            money.visibility = View.INVISIBLE
            time.visibility = View.INVISIBLE
            number.visibility = View.INVISIBLE
            icon.setImageResource(R.drawable.error_fail)
            tvResult.text = "转出失败！"
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    override fun onBackPressed() {
        return
        super.onBackPressed()
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, OutToWalletResult::class.java))
        }

        fun start(mContext: Context, money: String?, time: String?, runningWater: String?, type: String) {
            mContext.startActivity(Intent(mContext, OutToWalletResult::class.java).putExtra("profit", money).putExtra("time", time).putExtra("runningWater", runningWater).putExtra("type", type))
        }
    }
}