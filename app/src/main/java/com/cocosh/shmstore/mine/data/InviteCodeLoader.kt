package com.cocosh.shmstore.mine.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.InviteCodeContrat
import com.cocosh.shmstore.mine.model.InviteCodeModel
import com.cocosh.shmstore.utils.LogUtil

/**
 * Created by lmg on 2018/3/22.
 */
class InviteCodeLoader(val activity: BaseActivity, val inviteCodeView: InviteCodeContrat.IView) {
    fun requestInviteCodeData(flag: Int, userId: String, type: String) {

        val url = if (type == "2"){
            Constant.MYSELF_MATCHMAKER_INVITATION_DATA
        }else{
            ""
        }

        val map = HashMap<String, String>()
        map["userId"] = userId
        ApiManager2.get(flag, activity, map, url, object : ApiManager2.OnResult<BaseBean<InviteCodeModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<InviteCodeModel>) {
                inviteCodeView.inviteCodeData(data)
            }

            override fun onCatch(data: BaseBean<InviteCodeModel>) {
                LogUtil.d(data.toString())
            }
        })
    }
}