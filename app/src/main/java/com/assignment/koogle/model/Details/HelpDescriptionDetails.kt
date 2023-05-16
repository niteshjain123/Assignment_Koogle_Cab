package com.assignment.koogle.model.Details

import com.google.gson.annotations.SerializedName


data class HelpDescriptionDetails (
  @SerializedName("id"               ) var id              : Int?    = null,
  @SerializedName("user_help_id"     ) var userHelpId      : Int?    = null,
  @SerializedName("help_title"       ) var helpTitle       : String? = null,
  @SerializedName("help_description" ) var helpDescription : String? = null,
  @SerializedName("driver"           ) var driver          : Int?    = null,
  @SerializedName("created_at"       ) var createdAt       : String? = null,
  @SerializedName("updated_at"       ) var updatedAt       : String? = null
)