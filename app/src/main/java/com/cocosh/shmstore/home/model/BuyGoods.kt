package com.cocosh.shmstore.home.model

/**
 * 购买商品
 * Created by zhangye on 2018/9/3.
 */
data class BuyGoods(
        var no: String?,
        var ado_images: String?, // 商品图片
        var ado_name: String?,  // 商品名称
        var ado_price: String?, // 价格
        var ado_storage: String?,// 库存
        var address: Address?
) {
    data class Address(
            var receiver: String, // 收货人
            var phone: String, // 收货人手机
            var province: String, // 省
            var city: String, // 市
            var town: String, // 县
            var more: String  // 具体位置
    )
}
