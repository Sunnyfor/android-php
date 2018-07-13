package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class RedToWalletPresenter(var mActivity: BaseActivity, var mView: MineContrat.IRedToWalletView) : MineContrat.IRedToWalletPresenter {
    override fun requestRunningNumTo(flag:Int) {
        loader.requestRunningNumTo(flag)
    }

    override fun requestDrawTo(personType: String, amount: String, runningNum: String, paymentPassword: String) {
        loader.requestDrawTo(personType, amount, runningNum, paymentPassword)
    }

    override fun requestRedToWalletData(money: String, paymentPassword: String) {
        loader.requestRedToWallet(money, paymentPassword)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}