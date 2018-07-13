package com.cocosh.shmstore.mine.ui.authentication

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.RedPushData
import com.cocosh.shmstore.utils.LogUtil
import kotlinx.android.synthetic.main.activity_push_package_info.*

/**
 * Created by lmg on 2018/4/23.
 */
class PackagePushInfoActivity : BaseActivity() {
    var redPacketOrderId: String? = null
    override fun setLayout(): Int = R.layout.activity_push_package_info

    override fun initView() {
        titleManager.defaultTitle("投放数据")
        redPacketOrderId = intent.getStringExtra("redPacketOrderId")
        requestData(redPacketOrderId ?: "")
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
        requestData(redPacketOrderId ?: "")
    }

    fun requestData(id: String) {
        var map = HashMap<String, String>()
        map["redPacketOrderId"] = id
        ApiManager.get(1, this, map, Constant.BONUS_PUSH_DATA, object : ApiManager.OnResult<BaseModel<RedPushData>>() {
            override fun onSuccess(data: BaseModel<RedPushData>) {
                if (data.success && data.code == 200) {
                    push.text = data.entity?.totalMoney + "元"
                    pull.text = data.entity?.paidAmount + "元"
                    var ts1 = (data.entity?.totalMoney?.toDouble() ?: 2.0)
                    var ts2 = (data.entity?.paidAmount?.toDouble() ?: 1.0)
                    pullMoney.secondaryProgress = ((ts2 / ts1) * 100).toInt()

                    claim.text = data.entity?.redPacketNumber + "次"
                    realClaim.text = data.entity?.realRedPacketNumber + "次"
                    realClaimNumber.secondaryProgress = (((data.entity?.realRedPacketNumber?.toDouble()
                            ?: 1.0) / (data.entity?.redPacketNumber?.toDouble()
                            ?: 1.0)) * 100).toInt()

                    light.text = data.entity?.advertisingExposure + "次"
                    realLight.text = data.entity?.realAdvertisingExposure + "次"
                    realLightNumber.secondaryProgress = (((data.entity?.realAdvertisingExposure?.toDouble()
                            ?: 1.0) / (data.entity?.advertisingExposure?.toDouble()
                            ?: 1.0)) * 100).toInt()
                }
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<RedPushData>) {
                LogUtil.d(data.toString())
            }
        })
    }
}