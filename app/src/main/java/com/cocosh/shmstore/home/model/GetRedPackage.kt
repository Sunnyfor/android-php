package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/9/26.
 */

/**
 * hold字段含义
 *'1'-成功领取(注意:领取但未拆的有效期为10分钟),同时生成token
 *'2'-已占位已领取
 *'3'-未占位,跳转至<红包列表页UI>
 *'5'-已抢过该红包,跳转至<红包详情页UI>
 *'8'-已抢完,跳转至<哭脸UI>
 *'9'-数据异常,跳转至<数据异常UI>
 */

data class GetRedPackage(
        var hold: String?,  // 操作结果说明:(默认进入拆红包页)
        var tip: String?, // 占位提示语
        var token: String?, // 红包的操作令牌,用于后续操作,仅当`hold`=1时生效
        var time: String?, // 红包的领取时间,仅当`hold`=1时生效
        var expire: String? // 领取操作的生命周期(即:截止到<拆红包>操作之前的读秒时间),单位:秒,仅当`hold`=1时生效


)