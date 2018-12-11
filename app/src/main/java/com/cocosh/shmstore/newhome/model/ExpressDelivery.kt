package com.cocosh.shmstore.newhome.model


data class ExpressDelivery(
        var company:String,
        var com:String,
        var no:String,
        var status:String,
        var list:ArrayList<Data>


) {

    data class Data(
            var datetime:String,
            var remark:String,
            var zone:String

    )

}