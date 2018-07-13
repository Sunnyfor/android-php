package com.cocosh.shmstore.base


/**
 * MVP架构View层的base接口
 * Created by zhangye on 2017/10/20.
 */
interface IBaseView {
    //显示错误信息
    fun showError(type: Int)

    //显示进度
    fun showLoading()

    //隐藏进度
    fun hideLoading()

    //显示重试页面
    fun showReTryLayout()

    //显示重试页面
    fun hideReTryLayout()
}