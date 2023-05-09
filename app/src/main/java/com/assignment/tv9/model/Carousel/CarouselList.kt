package com.assignment.tv9.model.Carousel

import com.google.gson.annotations.SerializedName

data class CarouselList(
    @SerializedName("has_next" ) var hasNext : Boolean?          = null,
    @SerializedName("slider"   ) var slider  : ArrayList<CarouselSlider> = arrayListOf()
)
