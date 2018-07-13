package com.cocosh.shmstore.mine.model

/**
 *
 * Created by lmg on 2018/4/12.
 */
data class InterestModel(var idUserDescribeGroup: String?,
                         var interestGroupName: String?,
                         var interestGroupImg: String?,
                         var interestList: ArrayList<Data>?) {


    data class Data(var idUserDescribeGroup: String?,
                    var idUserInterestInfo: String?,
                    var interestName: String?,
                    var status:Int? =1)

}