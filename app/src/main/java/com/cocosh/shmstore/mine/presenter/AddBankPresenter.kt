package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class AddBankPresenter(var mActivity: BaseActivity, var mView: MineContrat.IAddBankView) : MineContrat.IAddBankPresenter {
    override fun requestAddBankData(idUserBankInfo: String,  messageCode: String) {
        loader.requestAddBankData(idUserBankInfo, messageCode)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}