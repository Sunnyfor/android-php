package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class MyWalletPresenter(var mActivity: BaseActivity, var mView: MineContrat.IMyWalletView) : MineContrat.IMyWalletPresenter {
    override fun requestDrawReslut(flag: Int) {
        loader.requestDrawReslut(flag)
    }

    override fun requestMyWalletData(flag: Int) {
        loader.requestMyWalletData(flag)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}