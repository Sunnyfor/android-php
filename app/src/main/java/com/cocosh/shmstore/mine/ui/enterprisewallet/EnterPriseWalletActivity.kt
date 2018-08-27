package com.cocosh.shmstore.mine.ui.enterprisewallet

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.EntWalletModel
import com.cocosh.shmstore.mine.presenter.EntWalletPresenter
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.mine.ui.authentication.IncomeActivity
import com.cocosh.shmstore.mine.ui.mywallet.MoneyWaterActivity
import com.cocosh.shmstore.mine.ui.mywallet.WithDrawActivity
import kotlinx.android.synthetic.main.activity_enterprise_wallet.*

/**
 *
 * Created by lmg on 2018/4/19.
 */
class EnterPriseWalletActivity : BaseActivity(), MineContrat.IEntWalletView {
    var mPresenter = EntWalletPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_enterprise_wallet
    override fun entWalletData(result: BaseBean<EntWalletModel>) {
            initData(result.message)
    }

    private fun initData(data: EntWalletModel?) {
        tvMoney.text = data?.balance
    }

    override fun initView() {
        titleManager.defaultTitle("服务商钱包")
        mPresenter.requestEntWalletData(1)
        btn_withdraw_money.setOnClickListener(this)
        btnServiceRevenue.setOnClickListener(this)
        moneyList.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btn_withdraw_money.id -> {
                WithDrawActivity.start(this, Constant.TYPE_ENTERPRISE, tvMoney.text.toString())
            }
            btnServiceRevenue.id -> {
                IncomeActivity.start(this, CommonType.FACILITATOR_INCOME.type)
            }
            moneyList.id -> {
                MoneyWaterActivity.start(this, Constant.TYPE_ENTERPRISE)
            }
        }

    }

    override fun reTryGetData() {
        mPresenter.requestEntWalletData(1)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, EnterPriseWalletActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        mPresenter.requestEntWalletData(1)
        super.onNewIntent(intent)
    }
}