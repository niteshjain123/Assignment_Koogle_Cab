package com.assignment.koogle.model.Dashboard

import com.google.gson.annotations.SerializedName


data class HelpList (
  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("title"      ) var title     : String? = null,
  @SerializedName("created_at" ) var createdAt : String? = null,
  @SerializedName("updated_at" ) var updatedAt : String? = null,
  @SerializedName("driver"     ) var driver    : Int?    = null
)