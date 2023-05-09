package com.assignment.tv9.model.Dashboard

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null
)
