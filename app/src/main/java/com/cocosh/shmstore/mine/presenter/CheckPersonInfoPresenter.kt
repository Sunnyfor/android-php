package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class CheckPersonInfoPresenter(var mActivity: BaseActivity, var mView: MineContrat.ICheckPersonInfoView) : MineContrat.ICheckPersonInfoPresenter {
    override fun requestCheckPersonInfoData(name: String, idCardNum: String) {
        loader.requestCheckPersonInfoData(name, idCardNum)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}