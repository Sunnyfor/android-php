package com.cocosh.shmstore.mine.ui.authentication

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.RedPushData
import com.cocosh.shmstore.utils.LogUtil
import kotlinx.android.synthetic.main.activity_push_package_info.*

/**
 *
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

    private fun requestData(id: String) {
        val map = HashMap<String, String>()
        map["no"] = id
        ApiManager2.get(1, this, map, Constant.SENDRP_LAUNCH_DATA, object : ApiManager2.OnResult<BaseModel<RedPushData>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseModel<RedPushData>) {
                if (data.success && data.code == 200) {
                    push.text = (data.entity?.amount + "元")
                    pull.text = (data.entity?.receive_amount + "元")
                    val ts1 = (data.entity?.amount?.toDouble() ?: 2.0)
                    val ts2 = (data.entity?.receive_amount?.toDouble() ?: 1.0)
                    pullMoney.secondaryProgress = ((ts2 / ts1) * 100).toInt()

                    claim.text = (data.entity?.total + "次")
                    realClaim.text = (data.entity?.receive_total + "次")
                    realClaimNumber.secondaryProgress = (((data.entity?.total?.toDouble()
                            ?: 1.0) / (data.entity?.receive_total?.toDouble()
                            ?: 1.0)) * 100).toInt()

//                    light.text = data.entity?.advertisingExposure + "次"
//                    realLight.text = data.entity?.realAdvertisingExposure + "次"
//                    realLightNumber.secondaryProgress = (((data.entity?.realAdvertisingExposure?.toDouble()
//                            ?: 1.0) / (data.entity?.advertisingExposure?.toDouble()
//                            ?: 1.0)) * 100).toInt()
                }
            }


            override fun onCatch(data: BaseModel<RedPushData>) {
                LogUtil.d(data.toString())
            }
        })
    }
}