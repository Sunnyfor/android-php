package com.cocosh.shmstore.newCertification.data

import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newCertification.contrat.CertificationContrat
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.NetworkUtils
import com.cocosh.shmstore.utils.ToastUtil
import java.io.File


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
                            if (idCardSide == IDCardParams.ID_CARD_SIDE_FRONT){
                                if (result.name == null || result.name.words.isNullOrEmpty() || result.idNumber == null || result.idNumber.words.isNullOrEmpty()){
                                    loginView.idCardResult(idCardSide, null)
                                    return
                                }
                            }else{
                                if (result.issueAuthority == null || result.issueAuthority.words.isNullOrEmpty()){
                                    loginView.idCardResult(idCardSide, null)
                                    return
                                }
                            }

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
}