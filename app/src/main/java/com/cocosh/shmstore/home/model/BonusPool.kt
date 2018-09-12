package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/5/16.
 */
data class BonusPool(var list: ArrayList<Data>?,
                     var banner: String,
                     var today: String) {

    data class Data(var name: String,
                    var type: String?,
                    var logo: String?,
                    var amount: String?,
                    var desc: String?)
}