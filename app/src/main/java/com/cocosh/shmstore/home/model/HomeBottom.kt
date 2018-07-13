package com.cocosh.shmstore.home.model

/**
 * 首页底部数据模型
 * Created by zhangye on 2018/6/5.
 */
data class HomeBottom(
        var redPacketAllAmount: Float, //红包总金额
        var resLightChinaVO: ResLightChinaVO?, //点亮中国信息
        var weatherEntity: WeatherEntity //天气信息
) {

    data class ResLightChinaVO(
            var lightChinaStatus: String, //点亮中国的状态
            var lightChinaUrl: String, //点亮中国的url
            var lightChinaNumberOfPeople: String? //点亮中国已参与的人数
    )

    data class WeatherEntity(
            var name: String?, //城市名称
            var weatherDetail: WeatherDetail, //天气详情
            var today:String?,
            var week:String
    ) {
        data class WeatherDetail(
                var text: String, //天气现象文字
                var code: String, //天气现象代码
                var temperature: String, // 温度，单位为c摄氏度或f华氏度
                var feels_like: String, //体感温度，单位为c摄氏度或f华氏度
                var pressure: String, //气压，单位为mb百帕或in英寸
                var humidity: String, //相对湿度，0~100，单位为百分比 ,
                var visibility: String, //能见度，单位为km公里或mi英里
                var wind_direction: String,//风向文字
                var wind_direction_degree: String, //风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西 ,
                var wind_speed: String, //风速，单位为km/h公里每小时或mph英里每小时
                var wind_scale: String, //风力等级
                var clouds: String,  //云量，范围0~100，天空被云覆盖的百分比 #目前不支持中国城市# ,
                var dew_point: String //露点温度，#目前不支持中国城市#
        )

    }
}