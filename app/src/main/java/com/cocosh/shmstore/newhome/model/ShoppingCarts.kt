package com.cocosh.shmstore.newhome.model

/**
 * 购物车
 */
data class ShoppingCarts(
        var store_id: String,
        var store_name: String,
        var goods_id: String,
        var goods_name: String,
        var sku_id: String,
        var sku_price: String,
        var sku_image: String,
        var sku_attrs:LinkedHashMap<String,String>,
        var num: String,
        var isChecked:Boolean = false,
        var goodsList: ArrayList<ShoppingCarts> = ArrayList()
) {
    override fun equals(other: Any?): Boolean {
        if (other is ShoppingCarts && other.store_name == this.store_name) {
            return true
        }
        return false
    }
}