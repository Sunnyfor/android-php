package com.cocosh.shmstore.model

/**
 *
 * Created by zhangye on 2018/8/9.
 */
data class CommonData(
        var phone: String,
        var nickname: String,
        var paypass: Int,
        var cert: Cert
) {
    data class Cert(
            var r: Int,
            var x: Int,
            var f: Int,
            var b: Int
    )
}