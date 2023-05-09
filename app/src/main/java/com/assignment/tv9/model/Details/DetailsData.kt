package com.assignment.tv9.model.Details

import com.google.gson.annotations.SerializedName


data class DetailsData (

  @SerializedName("id"             ) var id            : Int?              = null,
  @SerializedName("date"           ) var date          : String?           = null,
  @SerializedName("date_gmt"       ) var dateGmt       : String?           = null,
  @SerializedName("modified"       ) var modified      : String?           = null,
  @SerializedName("modified_gmt"   ) var modifiedGmt   : String?           = null,
  @SerializedName("slug"           ) var slug          : String?           = null,
  @SerializedName("status"         ) var status        : String?           = null,
  @SerializedName("type"           ) var type          : String?           = null,
  @SerializedName("link"           ) var link          : String?           = null,
  @SerializedName("content"        ) var content       : Content?          = Content(),
  @SerializedName("excerpt"        ) var excerpt       : Excerpt?          = Excerpt(),
  @SerializedName("author"         ) var author        : Int?              = null,
  @SerializedName("featured_media" ) var featuredMedia : Int?              = null,
  @SerializedName("comment_status" ) var commentStatus : String?           = null,
  @SerializedName("ping_status"    ) var pingStatus    : String?           = null,
  @SerializedName("sticky"         ) var sticky        : Boolean?          = null,
  @SerializedName("template"       ) var template      : String?           = null,
  @SerializedName("format"         ) var format        : String?           = null,
  @SerializedName("meta"           ) var meta          : ArrayList<String> = arrayListOf(),
  @SerializedName("categories"     ) var categories    : ArrayList<Int>    = arrayListOf(),
  @SerializedName("tags"           ) var tags          : ArrayList<Int>    = arrayListOf(),
  @SerializedName("amp_enabled"    ) var ampEnabled    : Boolean?          = null
)