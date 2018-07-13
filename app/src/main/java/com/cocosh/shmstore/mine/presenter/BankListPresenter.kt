package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class BankListPresenter(var mActivity: BaseActivity, var mView: MineContrat.IBankListView) : MineContrat.IBankListPresenter {
    override fun requestWouldDeleteBankData(flag: Int) {
        loader.requestWouldDeleteBankData(flag)
    }

    override fun requestDeleteBankData(cardId: String, pwd: String) {
        loader.requestDeleteBankData(cardId,pwd)
    }

    override fun requestBankListData(flag: Int) {
        loader.requestBankListData(flag)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}