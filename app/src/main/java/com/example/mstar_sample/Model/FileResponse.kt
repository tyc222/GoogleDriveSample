package com.example.mstar_sample.Model

import com.google.gson.annotations.SerializedName

    data class FileResponse(

        @SerializedName("kind")
        var kind:String,

        @SerializedName("id")
        var id:String,

        @SerializedName("name")
        var name:String,

        @SerializedName("mimeType")
        var mimeType:String,

        @SerializedName ("description")
        var description:String,

        @SerializedName ("createdDate")
        var createdDate:String,

        @SerializedName ("webContentLink")
        var webContentLink:String,

        @SerializedName ("webViewLink")
        var webViewLink:String,

        @SerializedName ("thumbnailLink")
        var thumbnailLink:String,

        @SerializedName ("shared")
        var shared:String,

        @SerializedName ("originalFilename")
        var originalFilename:String,

        @SerializedName("parents")
        var parents:ArrayList<String>

    )
