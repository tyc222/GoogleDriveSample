package com.example.mstar_sample.Model

import com.google.gson.annotations.SerializedName

data class TokenResponse (
    @SerializedName("access_token")
    var accessToken:String,

    @SerializedName("expires_in")
    var expiresIn:String,

    @SerializedName("refresh_token")
    var refreshToken:String,

    @SerializedName("scope")
    var scope:String,

    @SerializedName("token_type")
    var tokenType:String,

    @SerializedName("id_token")
    var idToken:String
)