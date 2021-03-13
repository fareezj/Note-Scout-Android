package com.wolf.notescout.data.model

import com.google.gson.annotations.SerializedName

class GroupRestData {
    data class GroupData (

            @SerializedName("id")
            var id: Int = 0,

            @SerializedName("groupId")
            var groupId: Int = 0,

            @SerializedName("groupOwner")
            var groupOwner: String = "",
    )
}