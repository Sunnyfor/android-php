package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusAmount
import com.cocosh.shmstore.home.model.BonusPool
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_bonus_money.view.*
import java.math.BigDecimal

/**
 * 红包总金额
 * Created by zhangye on 2018/4/18.
 */
class BonusMoneyFragment : BaseFragment() {
    private val tvList = arrayListOf<TextView>()
    override fun setLayout(): Int = R.layout.fragment_bonus_money

    override fun reTryGetData() {

    }

    override fun initView() {
        getLayoutView().setOnClickListener(this)
        tvList.add(getLayoutView().tv_1)
        tvList.add(getLayoutView().tv_2)
        tvList.add(getLayoutView().tv_3)
        tvList.add(getLayoutView().tv_4)
        tvList.add(getLayoutView().tv_5)
        tvList.add(getLayoutView().tv_6)
        tvList.add(getLayoutView().tv_7)
        tvList.add(getLayoutView().tv_8)
    }

    override fun onListener(view: View) {
        when (view.id) {
            getLayoutView().id -> {
                startActivity(Intent(context, BonusPoolActivity::class.java))
            }
        }
    }

    override fun close() {

    }


    private val positionCount = 8
    fun loadData(money: Float) {
        getLayoutView().tv_money.text = StringUtils.insertComma(money)
//        if (profit / 10000 > 10000) {
//            getLayoutView().tv_unit1.text = "亿"
//            getLayoutView().tv_unit2.text = "万"
//
//            profit.toString().let {
//                setValue(it, true)
//            }
//        } else {
//            getLayoutView().tv_unit1.text = "万"
//            getLayoutView().tv_unit2.text = "元"
//
//            profit.toString().let {
//                setValue(it, false)
//            }
//        }
    }


    private fun setValue(value: String, isBig: Boolean) {
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
}