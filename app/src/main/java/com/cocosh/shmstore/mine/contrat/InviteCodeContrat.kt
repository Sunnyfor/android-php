package com.cocosh.shmstore.mine.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.mine.model.InviteCodeModel

/**
 * Created by lmg on 2018/3/22.
 */
interface InviteCodeContrat {
    interface IView : IBaseView {
        fun inviteCodeData(result: BaseBean<InviteCodeModel>)
    }

    interface IPresenter : IBasePresenter {
        fun requestInviteCodeData(flag: Int, userId: String, type: String)
    }
}