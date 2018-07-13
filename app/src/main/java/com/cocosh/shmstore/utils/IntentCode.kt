package com.cocosh.shmstore.utils

/**
 * 跳转Code
 * Created by zhangye on 2018/1/27.
 */
object IntentCode {
    const val IS_TERM = 1  //跳转协议
    const val IS_REGIST = 2 //注册成功跳转（用于关闭注册验证码页面）
    const val IS_PHOTO = 5 //跳转相机
    const val IS_CAMERA = 3 //跳转相机
    const val IS_INPUT = 6
    const val LOCATION = 10 //定位
    const val CROP_PICTURE = 4 //裁剪图像
    const val IS_LOGOUT = 400
    const val FINISH = 0x000009  //关闭

    const val REQUEST_CODE_PICK_IMAGE = 100
    const val PERMISSIONS_REQUEST_CAMERA = 800
    const val PERMISSIONS_EXTERNAL_STORAGE = 801

    const val REQUEST_CODE_BUSINESS_LICENSE = 123
}