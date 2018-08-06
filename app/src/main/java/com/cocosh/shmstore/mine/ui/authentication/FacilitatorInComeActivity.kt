package com.cocosh.shmstore.mine.ui.authentication

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.OperatorMainData
import com.cocosh.shmstore.mine.ui.InviteCodeActivity
import com.cocosh.shmstore.utils.ToastUtil
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
        val params = HashMap<String, String>()
        ApiManager.get(flag, this, params, Constant.OPERATOR_MAIN, object : ApiManager.OnResult<BaseModel<OperatorMainData>>() {
            override fun onSuccess(data: BaseModel<OperatorMainData>) {
                hideLoading()
                if (data.success) {
                    setData(data.entity!!)
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                hideLoading()
            }

            override fun onCatch(data: BaseModel<OperatorMainData>) {
            }
        })
    }

    fun setData(data: OperatorMainData) {
        sCar.setValue(data.numPartnet)
        sEnt.setValue(data.numEnt)
        sPerson.setValue(data.numPerson)
        sGiveEnt.setValue(data.numEntBeDistribution)
        sGivePerson.setValue(data.numPersonBeDistribution)
        inCome.setValue(data.profit ?: "0.0"+"元")
        sInvite.setIcon(resources.getString(R.string.iconQrcode))
        name.setNoIconValue(data.name)
        area.setNoIconValue(data.serviceArea)
        time.setNoIconValue(data.attTime)
    }
}