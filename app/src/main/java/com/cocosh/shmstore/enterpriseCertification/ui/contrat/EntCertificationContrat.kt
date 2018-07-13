package com.cocosh.shmstore.enterpriseCertification.ui.contrat

import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.BankShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.InviteCodeModel

/**
 * Created by lmg on 2018/3/26.
 */
interface EntCertificationContrat {
    interface IView : IBaseView {
        fun setData(result: BaseModel<String>)
        fun setCodeData(result: BaseModel<InviteCodeModel>)
    }

    interface IPresenter : IBasePresenter {
        fun pushData(id: Long, companyName: String, inviteCode: String)
        fun getCodeData(flag: Int)
    }

    interface IBankView : IBaseView {
        fun setData(data: BaseModel<EntActiveInfoModel>)
    }

    interface IBankPresenter : IBasePresenter {
        fun pushData(bankCardImg: String, bankAccountNumber: String, accountOpeningBank: String, bankAccountName: String, mobilePhoneNumber: String, bankCardType: String)
    }

    interface IBankShowView : IBaseView {
        fun setData(data: BaseModel<BankShowBean>)
    }

    interface IBankShowPresenter : IBasePresenter {
        fun getData(flag:Int)
    }


}