package com.example.mstar_sample.Model

import com.google.gson.annotations.SerializedName

data class CloudServerResponse (
    @SerializedName("errcode")
    var errcode:Int,

    @SerializedName("rtoken")
    var rtoken:String
)