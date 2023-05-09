package com.assignment.tv9.model.Dashboard

import com.google.gson.annotations.SerializedName

data class Embedded(
    @SerializedName("author"           ) var author           : ArrayList<Author>           = arrayListOf()
)
