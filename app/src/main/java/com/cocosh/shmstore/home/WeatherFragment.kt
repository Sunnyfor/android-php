package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.model.HomeBottom
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.WeatherModel
import com.cocosh.shmstore.utils.LogUtil
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*
import java.util.HashMap

/**
 * 天气
 * Created by zhangye on 2018/4/18.
 */
class WeatherFragment : BaseFragment() {

    val iocnMap = hashMapOf<String,Int>()

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.fragment_weather

    override fun initView() {
        initIcon()
    }

    override fun onListener(view: View) {
    }

    override fun close() {

    }
//
//    /**
//     * 加载天气信息
//     */
//    fun loadWeather(city: String?) {
//        val params = hashMapOf<String, String>()
//        params["cityname"] = city ?: "北京"
//        params["key"] = Constant.WEATHER_KEY
//        ApiManager.get(0, activity as BaseActivity, params, ApiManager.weatherHost, object : ApiManager.OnResult<WeatherModel>() {
//            override fun onSuccess(data: WeatherModel) {
//                LogUtil.i("结果：$data")
//                if (data.resultcode == "200") {
//                    showWeather(data)
//                } else {
////                    getLayoutView().tvDesc.text = data.reason
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//
//            }
//
//            override fun onCatch(data: WeatherModel) {
//
//            }
//
//        })
//    }




    fun loadData(weather: HomeBottom.WeatherEntity) {
        val today = weather.today
        //当前温度
        if (getLayoutView().tv_now != null) {
            getLayoutView().tv_now.text = ("${weather.weatherDetail.temperature}°")
        }
        //星期
        try {
            getLayoutView().tv_week.text = today
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getLayoutView().tv_wealth.text = weather.weatherDetail.text
        getLayoutView().tv_day.text = weather.week

        iocnMap[weather.weatherDetail.code]?.let {
            ivPhoto.setImageResource(it)
        }
    }

    private fun initIcon(){
        iocnMap["0"] =  R.drawable.sunny_0_1_2_3
        iocnMap["1"] =  R.drawable.sunny_0_1_2_3
        iocnMap["2"] =  R.drawable.sunny_0_1_2_3
        iocnMap["3"] =  R.drawable.sunny_0_1_2_3
        iocnMap["4"] =  R.drawable.cloudy_4
        iocnMap["5"] =  R.drawable.partly_cloudy_5_6
        iocnMap["6"] =  R.drawable.partly_cloudy_5_6
        iocnMap["7"] =  R.drawable.mostly_cloudy_7_8
        iocnMap["8"] =  R.drawable.mostly_cloudy_7_8
        iocnMap["9"] =  R.drawable.overcast_9
        iocnMap["10"] =  R.drawable.shower_10
        iocnMap["11"] =  R.drawable.thundershower_11
        iocnMap["12"] =  R.drawable.thundershower_with_hail_12
        iocnMap["13"] =  R.drawable.light_rain_13
        iocnMap["14"] =  R.drawable.moderate_rain_14
        iocnMap["15"] =  R.drawable.heavy_rain_15
        iocnMap["16"] =  R.drawable.storm_16
        iocnMap["17"] =  R.drawable.heavys_torm_17
        iocnMap["18"] =  R.drawable.severe_storm_18
        iocnMap["19"] =  R.drawable.ice_rain_19
        iocnMap["20"] =  R.drawable.sleet_20
        iocnMap["21"] =  R.drawable.snow_flurry_21
        iocnMap["22"] =  R.drawable.light_snow_22
        iocnMap["23"] =  R.drawable.moderate_snow_23
        iocnMap["24"] =  R.drawable.heavy_snow_24
        iocnMap["25"] =  R.drawable.snowstorm_25
        iocnMap["26"] =  R.drawable.dust_26
        iocnMap["27"] =  R.drawable.sand_27
        iocnMap["28"] =  R.drawable.duststorm_28
        iocnMap["29"] =  R.drawable.sandstorm_29
        iocnMap["30"] =  R.drawable.foggy_30
        iocnMap["31"] =  R.drawable.haze_31
        iocnMap["32"] =  R.drawable.windy_32
        iocnMap["33"] =  R.drawable.blustery_33
        iocnMap["34"] =  R.drawable.hurricane_34
        iocnMap["35"] =  R.drawable.tropical_storm_35
        iocnMap["36"] =  R.drawable.tornado_36
        iocnMap["37"] =  R.drawable.cold_37
        iocnMap["38"] =  R.drawable.hot_38
    }

//    private var weathers1 = arrayOf(
//            R.drawable.sunny_0_1_2_3,
//            R.drawable.cloudy_4,
//            R.drawable.partly_cloudy_5_6,
//            R.drawable.mostly_cloudy_7_8,
//            R.drawable.leizhenyu1,
//            R.drawable.leizhenyubb1,
//            R.drawable.yujiaxue1,
//            R.drawable.xiaoyu1,
//            R.drawable.zhongyu1,
//            R.drawable.dayu1,
//            R.drawable.baoyu1,
//            R.drawable.dabaoyu1,
//            R.drawable.tdabaoyu1,
//            R.drawable.zhenxue1,
//            R.drawable.xiaoxue1,
//            R.drawable.zhenxue1,
//            R.drawable.daxue1,
//            R.drawable.baoxue1,
//            R.drawable.wu1,
//            R.drawable.dongyu1,
//            R.drawable.shachenbao1,
//            R.drawable.xiaoyu1,
//            R.drawable.zhongyu1,
//            R.drawable.dayu1,
//            R.drawable.baoyu1,
//            R.drawable.dabaoyu1,
//            R.drawable.xiaoxue1,
//            R.drawable.zhenxue1,
//            R.drawable.daxue1,
//            R.drawable.fuceng1,
//            R.drawable.yangsha1,
//            R.drawable.qshachenbao1)
//
//    var weathers = arrayOf(
//            R.drawable.qing,
//            R.drawable.duoyun,
//            R.drawable.yin,
//            R.drawable.zhenyu,
//            R.drawable.leizhenyu1,
//            R.drawable.leizhenyubb1,
//            R.drawable.yujiaxue1,
//            R.drawable.xiaoyu1,
//            R.drawable.zhongyu,
//            R.drawable.dayu,
//            R.drawable.baoyu,
//            R.drawable.dabaoyu,
//            R.drawable.tedabaoyu,
//            R.drawable.zhenxue,
//            R.drawable.xiaoxue,
//            R.drawable.zhenxue,
//            R.drawable.daxue,
//            R.drawable.baoxue,
//            R.drawable.wu,
//            R.drawable.dongyu,
//            R.drawable.shachenbao,
//            R.drawable.xiaoyu,
//            R.drawable.zhongyu,
//            R.drawable.dayu,
//            R.drawable.baoyu,
//            R.drawable.dabaoyu,
//            R.drawable.xiaoxue,
//            R.drawable.zhenxue,
//            R.drawable.daxue,
//            R.drawable.fuchen,
//            R.drawable.yangsha,
//            R.drawable.qshachenbao)

}