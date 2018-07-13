package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/5/8.
 */
class MyWalletDrawPresenter(var mActivity: BaseActivity, var mView: MineContrat.IMyWalletDrawView) : MineContrat.IMyWalletDrawPresenter {
    override fun requestRunningNum(flag: Int) {
        loader.requestRunningNum(flag)
    }

    override fun requestEntWalletDrawData(money: String, token: String) {
        loader.requestEntWalletDrawData(money, token)
    }

    override fun requestCorporateAccountData(flag: Int) {
        loader.requestCorporateAccountData(flag)
    }

    override fun requestMyWalletDrawData(userBankInfoId: String, amount: String, runningNum: String, paymentPassword: String) {
        loader.requestMyWalletDrawData(userBankInfoId, amount, runningNum, paymentPassword)
    }

    override fun requestBankListData() {
        loader.requestBankListData()
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}