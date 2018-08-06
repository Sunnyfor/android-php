package com.cocosh.shmstore.sms.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType

/**
 * 发送验证码网络处理类
 * Created by zhangye on 2018/8/2.
 */
class SMSLoader(var baseActivity: BaseActivity) {
    /**
     *   发送验证码
     */
    fun sendCode(phone: String, type: SMSType,oresult: ApiManager2.OnResult<BaseBean<SMS>>) {
        val map = HashMap<String, String>()
        map["phone"] = phone
        map["behavior"] = type.value
        ApiManager2.post(baseActivity, map, Constant.REGISTER_SEND_CODE, oresult)
    }
}