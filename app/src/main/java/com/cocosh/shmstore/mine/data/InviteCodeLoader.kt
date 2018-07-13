package com.cocosh.shmstore.mine.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.InviteCodeContrat
import com.cocosh.shmstore.mine.model.InviteCodeModel
import com.cocosh.shmstore.utils.LogUtil

/**
 * Created by lmg on 2018/3/22.
 */
class InviteCodeLoader(val activity: BaseActivity, val inviteCodeView: InviteCodeContrat.IView) {
    fun requestInviteCodeData(flag: Int, userId: String, type: String) {
        var map = HashMap<String, String>()
        map["userId"] = userId
        map["type"] = type
        ApiManager.get(flag, activity, map, Constant.INVITE_CODE_INFO, object : ApiManager.OnResult<BaseModel<InviteCodeModel>>() {
            override fun onSuccess(data: BaseModel<InviteCodeModel>) {
                inviteCodeView.inviteCodeData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.e(e.message.toString())
            }

            override fun onCatch(data: BaseModel<InviteCodeModel>) {
                LogUtil.d(data.toString())
            }
        })
    }
}