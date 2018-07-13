package com.cocosh.shmstore.utils

/**
 * 权限回调Code
 * Created by zhangye on 2018/1/22.
 */
enum class PermissionCode(val type: Int) {
    ALL(0),
    STORAGE(1), //内存卡读写Code
    CAMERA(2), //相机
    LOCATION(3),//定位
    PHONE(4),//拨打电话
    WRITE(5),//写内存卡
    READ(6)//读内存卡

}