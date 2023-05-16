package com.assignment.koogle.model.Dashboard

import com.google.gson.annotations.SerializedName

data class DashboardList(
    @SerializedName("helpList"   ) var helpList   : List<HelpList>,
    @SerializedName("message"    ) var message    : String?             = null,
    @SerializedName("statusCode" ) var statusCode : Int?                = null
)
