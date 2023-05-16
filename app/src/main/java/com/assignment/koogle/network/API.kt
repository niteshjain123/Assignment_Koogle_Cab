package com.assignment.koogle.network

import com.assignment.koogle.model.Dashboard.DashboardList
import com.assignment.koogle.model.Details.DetailsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("userhelplist")
    suspend fun getHelpListData() : Response<DashboardList>

    @GET("userhelpdescription")
    suspend fun getDetailsPage(@Query("user_help_id") user_help_id:Int) : Response<DetailsData>
}