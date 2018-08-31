package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.model.HomeBottom
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*

/**
 * 天气
 * Created by zhangye on 2018/4/18.
 */
class WeatherFragment : BaseFragment() {

    val iocnMap = hashMapOf<String, Int>()

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.fragment_weather

    override fun initView() {
        initBigImage()
        initSmallImage()
    }

    override fun onListener(view: View) {
    }

    override fun close() {

    }


    fun loadData(weather: HomeBottom.WeatherEntity) {
        val today = weather.week
        //当前温度
        if (getLayoutView().tv_now != null) {
            getLayoutView().tv_now.text = ("${weather.temp}°")
        }
        //星期
        try {
            getLayoutView().tv_week.text = today
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getLayoutView().tv_wealth.text = weather.weather
        getLayoutView().tv_day.text = weather.date_y

        val fa = weather.weather_id.fa
        val fb = weather.weather_id.fb

        if (fa == fb) {
            rl_sung.visibility = View.GONE
            iv_sung.visibility = View.VISIBLE
            bigImageMap[fa]?.let {
                iv_sung.setImageResource(it)
            }
        } else {
            iv_sung.visibility = View.GONE
            rl_sung.visibility = View.VISIBLE
            bigImageMap[fa]?.let {
                iv_fa.setImageResource(it)
            }
            smallImageMap[fb]?.let {
                iv_fb.setImageResource(it)
            }
        }
    }

//    private fun initIcon() {
//        iocnMap["0"] = R.drawable.sunny_0_1_2_3
//        iocnMap["1"] = R.drawable.sunny_0_1_2_3
//        iocnMap["2"] = R.drawable.sunny_0_1_2_3
//        iocnMap["3"] = R.drawable.sunny_0_1_2_3
//        iocnMap["4"] = R.drawable.cloudy_4
//        iocnMap["5"] = R.drawable.partly_cloudy_5_6
//        iocnMap["6"] = R.drawable.partly_cloudy_5_6
//        iocnMap["7"] = R.drawable.mostly_cloudy_7_8
//        iocnMap["8"] = R.drawable.mostly_cloudy_7_8
//        iocnMap["9"] = R.drawable.overcast_9
//        iocnMap["10"] = R.drawable.shower_10
//        iocnMap["11"] = R.drawable.thundershower_11
//        iocnMap["12"] = R.drawable.thundershower_with_hail_12
//        iocnMap["13"] = R.drawable.light_rain_13
//        iocnMap["14"] = R.drawable.moderate_rain_14
//        iocnMap["15"] = R.drawable.heavy_rain_15
//        iocnMap["16"] = R.drawable.storm_16
//        iocnMap["17"] = R.drawable.heavys_torm_17
//        iocnMap["18"] = R.drawable.severe_storm_18
//        iocnMap["19"] = R.drawable.ice_rain_19
//        iocnMap["20"] = R.drawable.sleet_20
//        iocnMap["21"] = R.drawable.snow_flurry_21
//        iocnMap["22"] = R.drawable.light_snow_22
//        iocnMap["23"] = R.drawable.moderate_snow_23
//        iocnMap["24"] = R.drawable.heavy_snow_24
//        iocnMap["25"] = R.drawable.snowstorm_25
//        iocnMap["26"] = R.drawable.dust_26
//        iocnMap["27"] = R.drawable.sand_27
//        iocnMap["28"] = R.drawable.duststorm_28
//        iocnMap["29"] = R.drawable.sandstorm_29
//        iocnMap["30"] = R.drawable.foggy_30
//        iocnMap["31"] = R.drawable.haze_31
//        iocnMap["32"] = R.drawable.windy_32
//        iocnMap["33"] = R.drawable.blustery_33
//        iocnMap["34"] = R.drawable.hurricane_34
//        iocnMap["35"] = R.drawable.tropical_storm_35
//        iocnMap["36"] = R.drawable.tornado_36
//        iocnMap["37"] = R.drawable.cold_37
//        iocnMap["38"] = R.drawable.hot_38
//    }


    private var bigImageMap = hashMapOf<String, Int>()
    private fun initBigImage() {
        bigImageMap["00"] = R.mipmap.qing1
        bigImageMap["01"] = R.mipmap.duoyun1
        bigImageMap["02"] = R.mipmap.yintian1
        bigImageMap["03"] = R.mipmap.zhenyu1
        bigImageMap["04"] = R.mipmap.leizhenyu1
        bigImageMap["05"] = R.mipmap.leizhenyubb1
        bigImageMap["06"] = R.mipmap.yujiaxue1
        bigImageMap["07"] = R.mipmap.xiaoyu1
        bigImageMap["08"] = R.mipmap.zhongyu1
        bigImageMap["09"] = R.mipmap.dayu1
        bigImageMap["10"] = R.mipmap.baoyu1
        bigImageMap["11"] = R.mipmap.dabaoyu1
        bigImageMap["12"] = R.mipmap.tdabaoyu1
        bigImageMap["13"] = R.mipmap.zhenxue1
        bigImageMap["14"] = R.mipmap.xiaoxue1
        bigImageMap["15"] = R.mipmap.zhenxue1
        bigImageMap["16"] = R.mipmap.daxue1
        bigImageMap["17"] = R.mipmap.baoxue1
        bigImageMap["18"] = R.mipmap.wu1
        bigImageMap["19"] = R.mipmap.dongyu1
        bigImageMap["20"] = R.mipmap.shachenbao1
        bigImageMap["21"] = R.mipmap.xiaoyu1
        bigImageMap["22"] = R.mipmap.zhongyu1
        bigImageMap["23"] = R.mipmap.dayu1
        bigImageMap["24"] = R.mipmap.baoyu1
        bigImageMap["25"] = R.mipmap.dabaoyu1
        bigImageMap["26"] = R.mipmap.xiaoxue1
        bigImageMap["27"] = R.mipmap.zhenxue1
        bigImageMap["28"] = R.mipmap.daxue1
        bigImageMap["29"] = R.mipmap.fuceng1
        bigImageMap["30"] = R.mipmap.yangsha1
        bigImageMap["31"] = R.mipmap.qshachenbao1
    }

    private var smallImageMap = hashMapOf<String, Int>()
    private fun initSmallImage() {
        smallImageMap["00"] = R.mipmap.qing
        smallImageMap["01"] = R.mipmap.duoyun
        smallImageMap["02"] = R.mipmap.yin
        smallImageMap["03"] = R.mipmap.zhenyu
        smallImageMap["04"] = R.mipmap.leizhenyu1
        smallImageMap["05"] = R.mipmap.leizhenyubb1
        smallImageMap["06"] = R.mipmap.yujiaxue1
        smallImageMap["07"] = R.mipmap.xiaoyu1
        smallImageMap["08"] = R.mipmap.zhongyu
        smallImageMap["09"] = R.mipmap.dayu
        smallImageMap["10"] = R.mipmap.baoyu
        smallImageMap["11"] = R.mipmap.dabaoyu
        smallImageMap["12"] = R.mipmap.tedabaoyu
        smallImageMap["13"] = R.mipmap.zhenxue
        smallImageMap["14"] = R.mipmap.xiaoxue
        smallImageMap["15"] = R.mipmap.zhenxue
        smallImageMap["16"] = R.mipmap.daxue
        smallImageMap["17"] = R.mipmap.baoxue
        smallImageMap["18"] = R.mipmap.wu
        smallImageMap["19"] = R.mipmap.dongyu
        smallImageMap["20"] = R.mipmap.shachenbao
        smallImageMap["21"] = R.mipmap.xiaoyu
        smallImageMap["22"] = R.mipmap.zhongyu
        smallImageMap["23"] = R.mipmap.dayu
        smallImageMap["24"] = R.mipmap.baoyu
        smallImageMap["25"] = R.mipmap.dabaoyu
        smallImageMap["26"] = R.mipmap.xiaoxue
        smallImageMap["27"] = R.mipmap.zhenxue
        smallImageMap["28"] = R.mipmap.daxue
        smallImageMap["29"] = R.mipmap.fuchen
        smallImageMap["30"] = R.mipmap.yangsha
        smallImageMap["31"] = R.mipmap.qshachenbao
    }
}