package com.cocosh.shmstore.enterpriseCertification.ui.data

import android.text.TextUtils
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseShowBean
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * Created by lmg on 2018/3/26.
 */
class EntLicenseLoader(var mActivity: BaseActivity, var mView: IBaseView) {
    /**
     * 获取概要信息
     */
    fun pushData(map: HashMap<String, String>) {
        ApiManager.post(mActivity, map, Constant.ENTERPRISE_INFO_LICENSE, object : ApiManager.OnResult<BaseModel<EntActiveInfoModel>>() {
            override fun onSuccess(data: BaseModel<EntActiveInfoModel>) {
                (mView as EntLicenseContrat.IView).setResultData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<EntActiveInfoModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    //获取七牛token
    fun getQINIUToken() {
        mActivity.showLoading()
        val map = java.util.HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.get(0,mActivity, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

            override fun onCatch(data: String) {}

            override fun onFailed(e: Throwable) {
                mActivity.hideLoading()
                LogUtil.d("获取token失败" + e)
            }

            override fun onSuccess(data: String) {
                mActivity.hideLoading()
                LogUtil.d("获取七牛Token结果：" + data)
                try {
                    val jsonObject = JSONObject(data)
                    val token = jsonObject.optString("token")
                    if (TextUtils.isEmpty(token)) {
                        ToastUtil.show("七牛Token为空")
                    } else {
                        (mView as EntLicenseContrat.IView).setQINIUToken(jsonObject.optString("token"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    LogUtil.d("获取token失败" + e)
                }
            }
        })
    }

    /**
     * 获取营业执照回显信息
     */
    fun getShowData(flag: Int) {
        ApiManager.get(flag, mActivity, HashMap(), Constant.ENTERPRISE_INFO_LICENSE_SHOW, object : ApiManager.OnResult<BaseModel<LicenseShowBean>>() {
            override fun onSuccess(data: BaseModel<LicenseShowBean>) {
                (mView as EntLicenseContrat.IShowView).setShowData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<LicenseShowBean>) {
                LogUtil.d(data.toString())
            }
        })
    }
}