package com.cocosh.shmstore.newCertification.model

data class EntityItem(val code: Int? = 0,
                      val areaName: String? = "",
                      val money: String? = "",
                      var isChecked: Int? = -1,
                      val status: String? = null)