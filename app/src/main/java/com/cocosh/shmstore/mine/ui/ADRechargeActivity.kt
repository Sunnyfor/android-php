package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.adapter.ADRechargeAdapter
import com.cocosh.shmstore.mine.model.ADRecharge
import kotlinx.android.synthetic.main.activity_ad_recharge.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class ADRechargeActivity : BaseActivity() {
    private var job: Job? = null
    private val list = arrayListOf<ADRecharge>()
    private val adapter: ADRechargeAdapter by lazy {
        ADRechargeAdapter(list)
    }

    var time = 10

    override fun setLayout(): Int = R.layout.activity_ad_recharge

    override fun initView() {
        titleManager.rightText("广告充值券", "活动规则", View.OnClickListener {

        })

        recyclerView.layoutManager = LinearLayoutManager(this)

        list.add(ADRecharge("3000000", "1000"))
        list.add(ADRecharge("2000000", "900"))
        list.add(ADRecharge("1000000", "5000"))
        list.add(ADRecharge("20000", "600"))
        list.add(ADRecharge("10000", "500"))
        recyclerView.adapter = adapter


        job = launch(CommonPool) {
            while (time >= 0) {
                val times = formatTime().split(":")
                launch(UI) {
                    tvH.text = times[0]
                    tvM.text = times[1]
                    tvS.text = times[2]
                    time--
                    if (time < 0) {
                        adapter.goneGive()
                        job?.cancel()
                    }
                }


                delay(1000)
            }
        }
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


    private fun formatTime(): String {
        var hour = 0
        var minute = 0
        var second = time

        if (second > 60) {
            minute = second / 60   //取整
            second %= 60   //取余
        }

        if (minute > 60) {
            hour = minute / 60
            minute %= 60
        }
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()
        val secondStr = if (second < 10) "0$second" else second.toString()

        return "$hourStr:$minuteStr:$secondStr"
    }

}