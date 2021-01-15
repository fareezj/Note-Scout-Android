package com.wolf.notescout.data.model

import com.google.gson.annotations.SerializedName

class NoteRestData {
    data class NoteData(

        @SerializedName("id")
        var id: Long = 0,

        @SerializedName("name")
        var item: String = "",

        @SerializedName("isChecked")
        var isChecked: Int = 0,

        @SerializedName("username")
        var username: String? = "",

        @SerializedName("groupID")
        var groupID: Int = 0
    )
}

