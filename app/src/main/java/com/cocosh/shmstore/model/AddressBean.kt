package com.cocosh.shmstore.model

import com.contrarywind.interfaces.IPickerViewData

/**
 *
 * Created by zhangye on 2018/4/11.
 */
class AddressBean : IPickerViewData {
    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */

    private var name = ""
    private var city: ArrayList<CityBean> = arrayListOf()

    fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    fun getCityList(): ArrayList<CityBean> = city

    fun setCityList(city: ArrayList<CityBean>) {
        this.city = city
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。


    class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */

        var name: String = ""
        var area: ArrayList<String> = arrayListOf()
    }

    override fun getPickerViewText(): String = name

}