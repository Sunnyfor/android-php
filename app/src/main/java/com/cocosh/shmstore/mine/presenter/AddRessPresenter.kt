package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class AddRessPresenter(var mActivity: BaseActivity, var mView: IBaseView) : MineContrat.IAddressPresenter {

    override fun requestAddAddress(id: String, receiver: String, phone: String, province: String, city: String, town: String, addr: String, default: String) {
        loader.requestAddAddress(id, receiver, phone, province, city,town,addr,default)
    }

    override fun requestDeleteAddress(flag: Int, idUserAddressInfo: String) {
        loader.requestDeleteAddress(idUserAddressInfo)
    }

    override fun requestGetAddress(flag: Int) {
        loader.requestGetAddress(flag)
    }

    override fun requestDefaultAddress(idUserAddressInfo: String) {
        loader.requestDefaultAddress(idUserAddressInfo)
    }


    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}