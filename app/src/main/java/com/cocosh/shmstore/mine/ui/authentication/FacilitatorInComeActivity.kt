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
import kotlinx.android.synthetic.main.activity_facilitator_income.*

/**
 * 服务商
 * Created by lmg on 2018/4/18.
 */
class FacilitatorInComeActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_facilitator_income

    override fun initView() {
        titleManager.defaultTitle("服务商")
        inCome.setOnClickListener(this)
        sInvite.setOnClickListener(this)

        sCar.setOnClickListener(this)
        sEnt.setOnClickListener(this)
        sPerson.setOnClickListener(this)
        sGiveEnt.setOnClickListener(this)
        sGivePerson.setOnClickListener(this)

        getThemeList(1)
    }

    override fun onListener(view: View) {
        when (view.id) {
            inCome.id -> {
                IncomeActivity.start(this, CommonType.FACILITATOR_INCOME.type)
            }
            sInvite.id -> {
                InviteCodeActivity.start(this,"3")
            }
            sCar.id -> {
                CommListActivity.start(this, CommonType.FACILITTOR_0.type)
            }
            sEnt.id -> {
                CommListActivity.start(this, CommonType.FACILITTOR_1.type)
            }
            sPerson.id -> {
                CommListActivity.start(this, CommonType.FACILITTOR_2.type)
            }
            sGiveEnt.id -> {
                CommListActivity.start(this, CommonType.FACILITTOR_3.type)
            }
            sGivePerson.id -> {
                CommListActivity.start(this, CommonType.FACILITTOR_4.type)
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {
        getThemeList(1)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, FacilitatorInComeActivity::class.java))
        }
    }

    private fun getThemeList(flag: Int) {
        ApiManager2.get(flag, this, null, Constant.MYSELF_PROVIDER_LIST, object : ApiManager2.OnResult<BaseBean<OperatorMainData>>() {
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
        sCar.setValue(data.expand_matchmaker_num)
        sEnt.setValue(data.expand_enterprise_num)
        sPerson.setValue(data.expand_personal_num)
        sGiveEnt.setValue(data.platform_enterprise_num)
        sGivePerson.setValue(data.platform_personal_num)
        inCome.setValue(data.profit ?: "0.0"+"元")
        sInvite.setIcon(resources.getString(R.string.iconQrcode))
        name.setNoIconValue(data.name)
        area.setNoIconValue(data.place)
        time.setNoIconValue(data.time)
    }
}