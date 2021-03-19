package com.wolf.notescout.data.model

import com.google.gson.annotations.SerializedName

class ResponseBody {
    data class Response(
            @SerializedName("message")
            var message: String = "",
    )
}