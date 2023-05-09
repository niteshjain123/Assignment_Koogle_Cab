package com.assignment.tv9.model.Dashboard

import com.google.gson.annotations.SerializedName


data class DetailsExcerpt (

  @SerializedName("rendered"  ) var rendered  : String?  = null,
  @SerializedName("protected" ) var protected : Boolean? = null

)