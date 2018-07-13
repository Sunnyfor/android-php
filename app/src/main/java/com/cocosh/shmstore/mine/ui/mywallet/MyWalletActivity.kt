package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.MyWalletModel
import com.cocosh.shmstore.mine.model.WithDrawResultModel
import com.cocosh.shmstore.mine.presenter.MyWalletPresenter
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_my_wallet.*

/**
 * Created by lmg on 2018/4/17.
 */
class MyWalletActivity : BaseActivity(), MineContrat.IMyWalletView {
    val mPresenter = MyWalletPresenter(this, this)
    override fun myWalletData(result: BaseModel<MyWalletModel>) {
        if (result.success && result.code == 200) {
            initData(result.entity)
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun drawReslut(result: BaseModel<WithDrawResultModel>) {
        if (result.success && result.code == 200) {
            WithDrawActivity.start(this, Constant.TYPE_MY, tvMoney.text.toString())
        } else {
            ToastUtil.show(result.message)
        }
    }

    private fun initData(data: MyWalletModel?) {
        tvMoney.text = data?.balance
        tvRedMoney.text = data?.redBalance
//        rule.text = "注：红包金额必须>" + data?.withdrawalMoney + "元时才可以提现!"
    }

    override fun setLayout(): Int = R.layout.activity_my_wallet

    override fun initView() {
        titleManager.facTitle("钱包", "银行卡").setRightOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                BankCardMangerActivity.start(this@MyWalletActivity)
            }
        })
        moneyList.setOnClickListener(this)
        btn_charge_money.setOnClickListener(this)
        btn_withdraw_money.setOnClickListener(this)
        rl_red.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            moneyList.id -> {
                MoneyWaterActivity.start(this, Constant.TYPE_MY)
            }
            btn_charge_money.id -> {
                ReChargeActivity.start(this)
            }
            btn_withdraw_money.id -> {
                //跳转提现
                WithDrawActivity.start(this, Constant.TYPE_MY, tvMoney.text.toString())
            }
            rl_red.id -> {
                RedAccountActivity.start(this)
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestMyWalletData(1)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, MyWalletActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.requestMyWalletData(1)
    }

    override fun onNewIntent(intent: Intent?) {
        mPresenter.requestMyWalletData(1)
        super.onNewIntent(intent)
    }
}