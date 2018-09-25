package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/5/16.
 */
data class UserInCome(var x_link:String?, //新媒人累计收益,依角色决定是否显示
                      var f_link:String?, //服务商累计收益,依角色决定是否显示
                      var rp:String?, // 红包收益
                      var x:String?, // 新媒人累计收益
                      var f:String?, // 服务商累计收益
                      var cert_x:String?,
                      var cert_f:String?,
                      var cert_b:String?)