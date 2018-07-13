package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class AddRessPresenter(var mActivity: BaseActivity, var mView: IBaseView) : MineContrat.IAddressPresenter {
    override fun requestDeleteAddress(flag: Int, idUserAddressInfo: String) {
        loader.requestDeleteAddress(flag, idUserAddressInfo)
    }

    override fun requestGetAddress(flag: Int) {
        loader.requestGetAddress(flag)
    }

    override fun requestDefaultAddress(idUserAddressInfo: String) {
        loader.requestDefaultAddress(idUserAddressInfo)
    }

    override fun requestAddAddress(idUserAddressInfo: String, addressName: String, addressPhone: String, areaCode: String, address: String) {
        loader.requestAddAddress(idUserAddressInfo, addressName, addressPhone, areaCode, address)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}