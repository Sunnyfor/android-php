package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.InviteCodeContrat
import com.cocosh.shmstore.mine.data.InviteCodeLoader

/**
 * Created by lmg on 2018/3/22.
 */
class InviteCodePresenter(val activity: BaseActivity, val inviteCodeView: InviteCodeContrat.IView) : InviteCodeContrat.IPresenter {
    val loader = InviteCodeLoader(activity, inviteCodeView)

    override fun requestInviteCodeData(flag: Int, userId: String, type: String) {
        loader.requestInviteCodeData(flag, userId, type)
    }

    override fun start() {

    }
}