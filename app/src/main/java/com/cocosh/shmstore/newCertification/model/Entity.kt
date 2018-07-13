package com.cocosh.shmstore.newCertification.model

data class Entity(val cardAddress: String? = "",
                  val ethnic: String? = "",
                  val address: String? = "",
                  val bizCode: String? = "",
                  val sex: String? = "",
                  val birth: String? = "",
                  val issuingAgency: String? = "",
                  val idNo: String? = "",
                  val realName: String? = "",
                  val validityPeriodEndTime: String? = "",
                  val validityPeriodStartTime: String? = "",
                  val money: Double? = 0.0,
                  val faceRecognition: String? = "")