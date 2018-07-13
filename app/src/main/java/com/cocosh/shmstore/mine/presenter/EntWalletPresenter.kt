package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntWalletPresenter(var mActivity: BaseActivity, var mView: MineContrat.IEntWalletView) : MineContrat.IEntWalletPresenter {
    override fun requestEntWalletData(flag: Int) {
        loader.requestEntWalletData(flag)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}