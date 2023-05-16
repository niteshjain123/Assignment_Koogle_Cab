package com.assignment.koogle.repository

import com.assignment.koogle.network.RetrofitInstance

class AppRepository {
    suspend fun getDashboardListData() = RetrofitInstance.koogleApi.getHelpListData()

    suspend fun getDetailsPageData(user_help_id:Int) = RetrofitInstance.koogleApi.getDetailsPage(user_help_id)

}