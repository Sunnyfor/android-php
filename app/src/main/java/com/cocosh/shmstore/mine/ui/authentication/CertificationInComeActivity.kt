package com.cocosh.shmstore.mine.ui.authentication

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.OperatorMainData
import com.cocosh.shmstore.mine.ui.InviteCodeActivity
import kotlinx.android.synthetic.main.activity_new_certification_income.*

/**
 *新媒人
 * Created by lmg on 2018/4/18.
 */
class CertificationInComeActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_new_certification_income

    override fun initView() {
        titleManager.defaultTitle("新媒人")
        inCome.setOnClickListener(this)
        sendRedPacket.setOnClickListener(this)

        sEnt.setOnClickListener(this)
        sPerson.setOnClickListener(this)
        sGivePerson.setOnClickListener(this)
        sInvite.setOnClickListener(this)
        getThemeList(1)
    }

    override fun onListener(view: View) {
        when (view.id) {
            inCome.id -> {
                IncomeActivity.start(this, CommonType.CERTIFICATION_INCOME.type)
            }
            sendRedPacket.id -> {
                SendPackageActivity.start(this)
            }
            sEnt.id -> {
                CommListActivity.start(this, CommonType.CERTIFICATION_0.type)
            }
            sPerson.id -> {
                CommListActivity.start(this, CommonType.CERTIFICATION_1.type)
            }
            sGivePerson.id -> {
                CommListActivity.start(this, CommonType.CERTIFICATION_2.type)
            }
            sInvite.id -> {
                InviteCodeActivity.start(this, "2")
            }
        }
    }

    override fun reTryGetData() {
        getThemeList(1)
    }

    private fun getThemeList(flag: Int) {
        val params = HashMap<String, String>()
        ApiManager2.get(flag, this, params, Constant.MYSELF_MATCHMAKER_LIST, object : ApiManager2.OnResult<BaseBean<OperatorMainData>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<OperatorMainData>) {
                data.message?.let {
                    setData(it)
                }
            }


            override fun onCatch(data: BaseBean<OperatorMainData>) {
            }
        })
    }

    fun setData(data: OperatorMainData) {
        sEnt.setValue(data.expand_enterprise_num)
        sPerson.setValue(data.expand_personal_num)
        sGivePerson.setValue(data.platform_personal_num)
        inCome.setValue(data.profit ?: "0.0"+"元")
        sInvite.setIcon(resources.getString(R.string.iconQrcode))
        name.setNoIconValue(data.name)
        area.setNoIconValue(data.place)
        time.setNoIconValue(data.time)
        sFacAttribute.setNoIconValue(data.provider_name)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, CertificationInComeActivity::class.java))
        }
    }
}