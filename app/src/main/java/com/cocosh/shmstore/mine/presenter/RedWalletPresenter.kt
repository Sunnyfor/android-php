package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 *
 * Created by lmg on 2018/3/26.
 */
class RedWalletPresenter(var mActivity: BaseActivity, var mView: MineContrat.IRedWalletView) : MineContrat.IRedWalletPresenter {
    override fun requestRedWalletData(flag: Int) {
        loader.requestRedWalletData(flag)
    }

    override fun requestRedWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String) {
        loader.requestRedWalletWater(flag, idUserAccountRecord, begTime)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }
}