package com.cocosh.shmstore.newCertification.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
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
    fun requestAddress(flag: Int, prov: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "x" //用户类型 (必填,'x'-新媒人,'f'-服务商; 2选1)
        map["prov"] = prov.toString()

        ApiManager2.post(flag, activity, map, Constant.SERVICE_REGION, object : ApiManager2.OnResult<BaseBean<ArrayList<AddressServiceModel>>>() {
            override fun onFailed(code: String, message: String) {
                addressView.addressResult(prov,null)
            }

            override fun onSuccess(data: BaseBean<ArrayList<AddressServiceModel>>) {
                addressView.addressResult(prov,data)
            }


            override fun onCatch(data: BaseBean<ArrayList<AddressServiceModel>>) {
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

    fun requestFacilitatorAddress(flag: Int, prov: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "f" //用户类型 (必填,'x'-新媒人,'f'-服务商; 2选1)
        map["prov"] = prov.toString()

        ApiManager2.post(flag, activity, map, Constant.SERVICE_REGION, object : ApiManager2.OnResult<BaseBean<ArrayList<AddressServiceModel>>>() {
            override fun onFailed(code: String, message: String) {
                addressView.addressResult(prov,null)
            }

            override fun onSuccess(data: BaseBean<ArrayList<AddressServiceModel>>) {
                addressView.addressFacResult(prov,data)
            }


            override fun onCatch(data: BaseBean<ArrayList<AddressServiceModel>>) {
                LogUtil.d(data.toString())
            }

        })

    }
}