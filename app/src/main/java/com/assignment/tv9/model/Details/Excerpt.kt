package com.assignment.tv9.model.Details

import com.google.gson.annotations.SerializedName


data class Excerpt (

  @SerializedName("rendered"  ) var rendered  : String?  = null,
  @SerializedName("protected" ) var protected : Boolean? = null

)