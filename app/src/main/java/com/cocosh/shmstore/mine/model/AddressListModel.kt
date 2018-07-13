package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/11.
 *  address (string, optional): 详细收获地址 ,
addressName (string, optional): 收货人姓名 ,
addressPhone (string, optional): 收货人手机号 ,
areaCode (string, optional): 区域编码 ,
idUserAddressInfo (integer, optional): 用户地址id,新增时候不用填写该字段 ,
isDefault (integer, optional): 是否为默认地址(0-不是 1-是)
 *
 */
data class AddressListModel(var address: String?,
                            var addressName: String? ,
                            var addressPhone: String?,
                            var areaCode: String?,
                            var idUserAddressInfo: String?,
                            var isDefault: String?,
                            var areaName: String?)