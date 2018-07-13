package com.cocosh.shmstore.newCertification.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.contrat.AddressContrat
import com.cocosh.shmstore.newCertification.model.AddressServiceModel
import com.cocosh.shmstore.newCertification.model.ApplyPartner
import com.cocosh.shmstore.utils.LogUtil

/**
 *
 * Created by cjl on 2018/2/6.
 */
class AddressLoader(val activity: BaseActivity, val addressView: AddressContrat.IView) {
    fun requestAddress(flag: Int, code: Int) {
        val map = HashMap<String, String>()

        map["parentCode"] = code.toString()

        ApiManager.get(flag, activity, map, Constant.SERVICE_ADDRESS, object : ApiManager.OnResult<BaseModel<ArrayList<AddressServiceModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<AddressServiceModel>>) {
                addressView.addressResult(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<AddressServiceModel>>) {
                LogUtil.d(data.toString())
            }

        })

    }

    fun commitAddress(code: String) {
//        val map = HashMap<String, String>()
//        map["addressCode"] = code
//        map["beInviteCode"] = "邀请码"
//        map["operatorId"] = "服务商ID"
//        ApiManager.post(activity,map,Constant.COMMIT_ADDRESS,object :ApiManager.OnResult<BaseModel<ApplyPartner>>(){
//            override fun onSuccess(data: BaseModel<ApplyPartner>) {
//                addressView.commitAddressResult(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                LogUtil.d(e.message.toString())
//
//            }
//
//            override fun onCatch(data: BaseModel<ApplyPartner>) {
//
//            }
//
//        })
    }

    fun requestFacilitatorAddress(flag: Int, code: Int) {
        val map = HashMap<String, String>()
        map["parentCode"] = code.toString()
        ApiManager.get(flag, activity, map, Constant.FACILITOTAAR_INFO_ADDRESS, object : ApiManager.OnResult<BaseModel<ArrayList<AddressServiceModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<AddressServiceModel>>) {
                addressView.addressFacResult(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<AddressServiceModel>>) {
                LogUtil.d(data.toString())
            }

        })
    }
}