package com.cocosh.shmstore.newCertification.model

data class AddressServiceModel(var user_type: String,  // 用户类型 (必填,'x'-新媒人,'f'-服务商)
                               var code: String = "", // 地区代码
                               var name: String = "", // 地区名称
                               var isChecked: Int = -1,
                               var fee: String, // 认证费用
                               var full: Int)// 是否已满