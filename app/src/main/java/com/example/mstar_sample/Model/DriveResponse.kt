package com.example.mstar_sample.Model

import com.google.gson.annotations.SerializedName

    data class DriveResponse (
        @SerializedName("kind")
        var kind:String,

        @SerializedName("nextPageToken")
        var nextPageToken:String,

        @SerializedName("incompleteSearch")
        var incompleteSearch:String,

        @SerializedName("files")
        var files:ArrayList<FileResponse>
    )
