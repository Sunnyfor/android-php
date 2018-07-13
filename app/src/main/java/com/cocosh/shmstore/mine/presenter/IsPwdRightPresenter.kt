package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class IsPwdRightPresenter(var mActivity: BaseActivity, var mView: MineContrat.IIsPwdRightView) : MineContrat.IIsPwdRightPresenter {
    override fun requestModifyPwdData(pwd: String, repwd: String) {
        loader.requestModifyPwdData(pwd, repwd)
    }

    override fun requestSavePwdData(pwd: String, repwd: String) {
        loader.requestSavePwdData(pwd, repwd)
    }

    override fun requestIsPwdRightData(pwd: String) {
        loader.requestIsPwdRightData(pwd)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}