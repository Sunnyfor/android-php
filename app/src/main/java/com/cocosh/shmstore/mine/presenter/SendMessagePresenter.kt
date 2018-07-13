package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class SendMessagePresenter(var mActivity: BaseActivity, var mView: MineContrat.ISendMessageView) : MineContrat.ISendMessagePresenter {
    override fun requestAuthCodeData(code: String) {
        loader.requestAuthCodeData(code)
    }

    override fun requestSendMessageData(phone: String, boolean: Boolean) {
        loader.requestSendMessageData(phone, boolean)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}