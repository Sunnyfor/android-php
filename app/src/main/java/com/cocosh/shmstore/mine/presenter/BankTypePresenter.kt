package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class BankTypePresenter(var mActivity: BaseActivity, var mView: MineContrat.IBankTypeView) : MineContrat.IBankTypePresenter {
    override fun requestBankInfoCheck(flag: Int, bank_kind:String,cardNumber: String, cardUserName: String, cardUserPhone: String) {
        loader.requestCheckBankInfo(flag, bank_kind,cardNumber,cardUserPhone)
    }

    override fun requestBankTypeData(flag: Int) {
        loader.requestBankTypeData(flag)
    }


    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}