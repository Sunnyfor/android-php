package com.cocosh.shmstore.home.model

/**
 * 修改红包数据模型
 * Created by zhangye on 2018/8/31.
 */
data class ModifyBonus(
        var rp_id: String,//红包ID
        var no: String,
        var name: String,
        var image: String,
        var pubtime: String, //投放开始时间
        var ado_desc: String,//简述
        var ado_images: String, //广告内容
        var ado_slider_master: String,
        var amount: String,// 总金额
        var clicks: String,//红包领取次数->进过讨论改成 红包点击次数
        var exposures: String,//广告曝光次数
        var pos_city: String,//投放位置-城市
        var pos_prov: String,//投放位置-省份
        var price: String,// 单个红包平均金额
        var total: String //红包个数
)