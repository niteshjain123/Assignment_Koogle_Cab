package com.assignment.tv9.model.Carousel

import com.google.gson.annotations.SerializedName

data class CarouselSlider(
    @SerializedName("id"                     ) var id                   : Int?     = null,
    @SerializedName("title"                  ) var title                : CarouselTitle?   = CarouselTitle(),
    @SerializedName("short_headline"         ) var shortHeadline        : String?  = null,
    @SerializedName("category_name"          ) var categoryName         : String?  = null,
    @SerializedName("category_name_english"  ) var categoryNameEnglish  : String?  = null,
    @SerializedName("format"                 ) var format               : String?  = null,
    @SerializedName("web_featured_image"     ) var webFeaturedImage     : String?  = null,
    @SerializedName("mobile_featured_image"  ) var mobileFeaturedImage  : String?  = null,
    @SerializedName("is_vidgyor"             ) var isVidgyor            : Boolean? = null,
    @SerializedName("episode_no"             ) var episodeNo            : Int?     = null,
    @SerializedName("season_no"              ) var seasonNo             : Int?     = null,
    @SerializedName("show_details_available" ) var showDetailsAvailable : Boolean? = null
)
