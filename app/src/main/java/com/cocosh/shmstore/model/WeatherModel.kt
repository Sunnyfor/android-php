package com.cocosh.shmstore.model

/**
 * 天气预报实体类
 * Created by zhangye on 2018/4/18.
 */
data class WeatherModel(
        var resultcode: String,
        var reason: String,
        var result: Result
) {
    data class Result(
            var sk: Sk,
            var today: Today
    ) {
        /*当前实况天气*/
        data class Sk(
                var temp: String, /*当前温度*/
                var wind_direction: String, /*当前风向*/
                var wind_strength: String, /*当前风力*/
                var humidity: String, /*当前湿度*/
                var time: String /*更新时间*/
        )

        data class Today(
                var city: String,
                var date_y: String,
                var week: String,
                var temperature: String, /*今日温度*/
                var weather: String, /*今日天气*/
                var weather_id: WeatherId /*天气唯一标识*/,
                var wind: String,
                var dressing_index: String,  /*穿衣指数*/
                var dressing_advice: String,/*穿衣建议*/
                var uv_index: String, /*紫外线强度*/
                var comfort_index: String, /*舒适度指数*/
                var wash_index: String, /*洗车指数*/
                var travel_index: String, /*旅游指数*/
                var exercise_index: String, /*晨练指数*/
                var drying_index: String /*干燥指数*/
        ) {
            data class WeatherId(
                    var fa: String,/*天气标识00：晴*/
                    var fb: String /*天气标识53：霾 如果fa不等于fb，说明是组合天气*/
            )
        }


    }


}