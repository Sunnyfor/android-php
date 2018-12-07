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

    override fun hashCode(): Int {
        var result = store_id.hashCode()
        result = 31 * result + store_name.hashCode()
        result = 31 * result + goods_id.hashCode()
        result = 31 * result + goods_name.hashCode()
        result = 31 * result + sku_id.hashCode()
        result = 31 * result + sku_price.hashCode()
        result = 31 * result + sku_image.hashCode()
        result = 31 * result + sku_attrs.hashCode()
        result = 31 * result + num.hashCode()
        result = 31 * result + isChecked.hashCode()
        result = 31 * result + goodsList.hashCode()
        return result
    }
}