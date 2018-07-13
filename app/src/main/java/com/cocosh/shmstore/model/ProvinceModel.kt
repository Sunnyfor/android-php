package com.cocosh.shmstore.model

import com.contrarywind.interfaces.IPickerViewData

/**
 *
 * Created by zhangye on 2018/5/8.
 */
data class ProvinceModel(var name: String,
                         var id: String,
                         var childrens: ArrayList<ProvinceModel>?) : IPickerViewData {
    override fun getPickerViewText(): String = name

}