package com.cocosh.shmstore.mine.ui.mywallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.enterprisewallet.EnterPriseWalletActivity
import kotlinx.android.synthetic.main.activity_withdraw_result.*

/**
 * Created by lmg on 2018/4/18.
 */
class WithDrawResult : BaseActivity() {
    private var TYPE_WITHDRAW = ""//type_my我的钱包流水"type_enterprise"企业liushui
    override fun setLayout(): Int = R.layout.activity_withdraw_result

    @SuppressLint("SetTextI18n")
    override fun initView() {
        TYPE_WITHDRAW = intent.getStringExtra("TYPE_WITHDRAW")
        titleManager.rightText("提现结果", "完成", View.OnClickListener {
            if (TYPE_WITHDRAW == Constant.TYPE_MY) {
                MyWalletActivity.start(this@WithDrawResult)
            } else {
                EnterPriseWalletActivity.start(this@WithDrawResult)
            }
        }, true)

        var amount: String? = intent.getStringExtra("amount")
        var charge: String? = intent.getStringExtra("charge")
        var cardNumber: String? = intent.getStringExtra("cardNumber")
        var dealBeginDate: String? = intent.getStringExtra("dealBeginDate")
        var runningNum: String? = intent.getStringExtra("runningNum")
        var resultDesc: String? = intent.getStringExtra("resultDesc")
        var estimatedaArrivalTime: String? = intent.getStringExtra("estimatedaArrivalTime")
        var resultCode: String? = intent.getStringExtra("resultCode")
        var bankNameValue: String? = intent.getStringExtra("name")
        var userBankName: String? = intent.getStringExtra("userBankName")

        money.setNoIconValue(amount + "元")
        bankMoney.setNoIconValue(charge + "元")
        time.setNoIconValue(dealBeginDate)
        number.setNoIconValue(runningNum)
        bankName.setNoIconValue(bankNameValue)
        name.setNoIconValue(userBankName)
        toTime.text = "预计到账时间:$estimatedaArrivalTime"

        if (TYPE_WITHDRAW == Constant.TYPE_MY) {
            account.setNoIconValue("$bankNameValue 尾号" + cardNumber?.substring(cardNumber?.length - 4, cardNumber?.length))
        } else {
            account.setNoIconValue(cardNumber)
            bankName.visibility = View.VISIBLE
            name.visibility = View.VISIBLE
        }
        if (resultCode == "2") {
            line_two.setBackgroundResource(R.color.red)
            ivTwo.visibility = View.VISIBLE
            tvTwoIcon.visibility = View.INVISIBLE
            tvThreeIcon.visibility = View.INVISIBLE
            ivThree.visibility = View.VISIBLE
            tvThree.text = "到账失败"
            tvThree.setTextColor(resources.getColor(R.color.blackText))
        } else {
            line_two.setBackgroundResource(R.color.grayText)
            ivTwo.visibility = View.INVISIBLE
            tvTwoIcon.visibility = View.VISIBLE
            tvThreeIcon.visibility = View.VISIBLE
            ivThree.visibility = View.INVISIBLE
            tvThree.text = "到账成功"
            tvThree.setTextColor(resources.getColor(R.color.grayText))
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
        fun start(mContext: Context, type: String, amount: String,
                  charge: String, cardNumber: String, dealBeginDate: String,
                  estimatedaArrivalTime: String, runningNum: String, resultCode: String, bankName: String, userBankName: String) {
            mContext.startActivity(Intent(mContext, WithDrawResult::class.java)
                    .putExtra("TYPE_WITHDRAW", type)
                    .putExtra("amount", amount)
                    .putExtra("charge", charge)
                    .putExtra("cardNumber", cardNumber)
                    .putExtra("dealBeginDate", dealBeginDate)
                    .putExtra("estimatedaArrivalTime", estimatedaArrivalTime)
                    .putExtra("runningNum", runningNum)
                    .putExtra("resultCode", resultCode)
                    .putExtra("name", bankName)
                    .putExtra("userBankName", userBankName))
        }
    }
}