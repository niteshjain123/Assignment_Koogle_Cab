package com.assignment.koogle.model.Details

import com.google.gson.annotations.SerializedName


data class DetailsData (
  @SerializedName("helpDescriptionDetails" ) var helpDescriptionDetails : List<HelpDescriptionDetails>,
  @SerializedName("message"                ) var message                : String?                           = null,
  @SerializedName("statusCode"             ) var statusCode             : Int?                              = null
)