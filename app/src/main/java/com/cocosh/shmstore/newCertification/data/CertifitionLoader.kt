package com.cocosh.shmstore.newCertification.data

import android.text.TextUtils
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.contrat.CertificationContrat
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.NetworkUtils
import com.cocosh.shmstore.utils.ToastUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap


class CertifitionLoader(var activity: BaseActivity, var loginView: CertificationContrat.IView) {

    //识别身份证请求
    fun recIDCard(idCardSide: String, filePath: String) {
        activity.runOnUiThread {
            if (!NetworkUtils.isNetworkAvaliable(activity)) {
                ToastUtil.show(activity.getString(R.string.networkError))
            } else {
                activity.showLoading()
                val param = IDCardParams()
                param.imageFile = File(filePath)
                // 设置身份证正反面
                param.idCardSide = idCardSide
                // 设置方向检测
                param.isDetectDirection = true
                // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
                //param.imageQuality = 20
                val instance = OCR.getInstance()
                instance.recognizeIDCard(param, object : OnResultListener<IDCardResult> {
                    override fun onResult(result: IDCardResult?) {
                        activity.hideLoading()
                        if (result != null) {
                            loginView.idCardResult(idCardSide, result)
                        }
                    }

                    override fun onError(error: OCRError) {
                        activity.hideLoading()
                        loginView.idCardResult(idCardSide, null)
                        LogUtil.d("身份证识别错误${error.message}")
                    }
                })
            }
        }
    }

    //获取七牛token
    fun tokenRequest() {
        activity.showLoading()
        val map = HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.get(0,activity, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

            override fun onCatch(data: String) {}

            override fun onFailed(e: Throwable) {
                activity.hideLoading()
                LogUtil.d("获取token失败" + e)
            }

            override fun onSuccess(data: String) {
                activity.hideLoading()
                LogUtil.d("获取七牛Token结果：" + data)
                try {
                    val jsonObject = JSONObject(data)
                    val token = jsonObject.optString("token")
                    if (TextUtils.isEmpty(token)) {
                        ToastUtil.show("七牛Token为空")
                    } else {
                        loginView.tokenResult(jsonObject.optString("token"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }
}