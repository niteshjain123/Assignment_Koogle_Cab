package com.assignment.tv9.repository

import com.assignment.tv9.network.RetrofitInstance

class AppRepository {
    suspend fun getCarouselData() = RetrofitInstance.picsumApi.getCarousel()

    suspend fun getDashboardListData() = RetrofitInstance.picsumApi.getDashboardList()

    suspend fun getDetailsPageData() = RetrofitInstance.picsumApi.getDetailsPage("article_id")

    suspend fun getDetailsPageDataApi2() = RetrofitInstance.picsumApi.getDetailsPageApi2("https://www.tv9hindi.com/wp-json/wp/v2/posts/1828503?token=saaKkv5v@eW7F\$YKTbROu3e7")

    suspend fun getDetailsPageDataApi1() = RetrofitInstance.picsumApi.getDetailsPageApi1("https://www.tv9hindi.com/wp-json/wp/v2/posts/1828517?token=saaKkv5v@eW7F\$YKTbROu3e7")

    suspend fun getDetailsPageDataApi3() = RetrofitInstance.picsumApi.getDetailsPageApi1("https://www.tv9hindi.com/wp-json/wp/v2/posts/1852841?token=saaKkv5v@eW7F\$YKTbROu3e7")

}