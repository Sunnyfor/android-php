package com.cocosh.shmstore.forgetPsd.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DigestUtils

/**
 * 验证手机
 * Created by zhangye on 2018/1/25.
 */
class IdentifyMobileLoader(private var baseActivity: BaseActivity, var iView: IdentifyMobileContract.IView) {

    //忘记密码
    fun forgetPass(phone: String, password: String, smscode: String, smskey: String, onResult: ApiManager2.OnResult<BaseBean<String>>) {
        val map = HashMap<String, String>()
        map["phone"] = phone    //手机号 (必填)
        map["passwd"] = DigestUtils.md5(password) // 密码 (必填, 密文, 加密方式: md5[明文])
        map["smscode"] = smscode //短信验证码 (必填, 由短信发送接口反馈给客户端)
        map["smskey"] = smskey
        ApiManager2.post(baseActivity, map, Constant.FORGOTPWD, onResult)
    }
}