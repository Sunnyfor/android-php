package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.BonusMoneyPoolAdapter
import com.cocosh.shmstore.home.model.BonusAmount
import com.cocosh.shmstore.home.model.BonusPool
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.StringUtils
import kotlinx.android.synthetic.main.activity_bonus_pool.*
import java.math.BigDecimal

/**
 * 红包资金池
 * Created by zhangye on 2018/4/19.
 */
class BonusPoolActivity : BaseActivity() {

    private val tvList = arrayListOf<TextView>()

    override fun setLayout(): Int = R.layout.activity_bonus_pool
    override fun initView() {
        titleManager.defaultTitle("红包资金池")

        tvList.add(tv_1)
        tvList.add(tv_2)
        tvList.add(tv_3)
        tvList.add(tv_4)
        tvList.add(tv_5)
        tvList.add(tv_6)
        tvList.add(tv_7)
        tvList.add(tv_8)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    private val positionCount = 8
    fun setValue(value: String, isBig: Boolean) {
        val sb = StringBuilder()
        val numberStr = BigDecimal(value).toLong().toString()
        val maxLenght: Int = if (isBig) {
            12
        } else {
            8
        }

        if (numberStr.length <= maxLenght) {
            for (i in 0 until maxLenght - numberStr.length) {
                sb.append("0")
            }
            sb.append(numberStr)
            for (i in 0 until positionCount) {
                tvList[i].text = sb[i].toString()
            }
        }
    }


    fun loadData() {
        val params = hashMapOf<String, String>()
        ApiManager.get(1, this, params, Constant.BONUS_AMOUNT, object : ApiManager.OnResult<BaseModel<BonusPool>>() {
            override fun onSuccess(data: BaseModel<BonusPool>) {
                if (data.success) {
                    data.entity?.let {
                        GlideUtils.loadRect(this@BonusPoolActivity,it.redPacketPicture,ivPhoto)
                        recyclerView.adapter = BonusMoneyPoolAdapter(it.resPopularRedEnvelopesList)
                        tv_money.text = StringUtils.insertComma(it.redPacketAllAmount)
//                        if (it.redPacketAllAmount / 10000 > 10000) {
//                            tv_unit1.text = "亿"
//                            tv_unit2.text = "万"
//
//                            it.redPacketAllAmount.toString().let {
//                                setValue(it, true)
//                            }
//
//                        } else {
//                            tv_unit1.text = "万"
//                            tv_unit2.text = "元"
//
//                            (it.redPacketAllAmount).toString().let {
//                                setValue(it, false)
//                            }
//                        }
                    }
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<BonusPool>) {

            }

        })
    }
}