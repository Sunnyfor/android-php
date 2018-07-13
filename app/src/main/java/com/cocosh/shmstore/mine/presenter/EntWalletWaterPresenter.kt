package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 * 企业钱包流水
 */
class EntWalletWaterPresenter(var mActivity: BaseActivity, var mView: MineContrat.IEntWalletWaterView) : MineContrat.IEntWalletWaterPresenter {
    override fun requestWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        loader.requestWalletWaterData(flag, idUserAccountRecord, begTime, showCount)
    }

    override fun requestEntWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        loader.requestEntWalletWaterData(flag, idUserAccountRecord, begTime, showCount)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}