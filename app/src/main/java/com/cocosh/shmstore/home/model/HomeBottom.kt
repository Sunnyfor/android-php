package com.cocosh.shmstore.home.model

/**
 * 首页底部数据模型
 * Created by zhangye on 2018/6/5.
 */
data class HomeBottom(
        var redPacketAllAmount: Float, //红包总金额
        var resLightChinaVO: ResLightChinaVO?, //点亮中国信息
        var weather: WeatherEntity //天气信息
) {

    data class ResLightChinaVO(
            var lightChinaStatus: String, //点亮中国的状态
            var lightChinaUrl: String, //点亮中国的url
            var lightChinaNumberOfPeople: String? //点亮中国已参与的人数
    )

    data class WeatherEntity(
            var temp: String, // 温度，单位为c摄氏度或f华氏度
            var temperature: String, //天气现象文字
            var weather: String, //天气现象代码
            var week: String, //体感温度，单位为c摄氏度或f华氏度
            var date_y: String,
            var weather_id: WeatherId
    ) {
        data class WeatherId(
                var fa: String,
                var fb: String
        )
    }

}