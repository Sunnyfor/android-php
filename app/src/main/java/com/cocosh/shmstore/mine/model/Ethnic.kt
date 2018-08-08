package com.cocosh.shmstore.mine.model

import com.contrarywind.interfaces.IPickerViewData

/**
 * 民族
 * Created by zhangye on 2018/8/8.
 */
data class Ethnic(
        var id: String,
        var name: String
) : IPickerViewData {
    override fun getPickerViewText(): String = name
}