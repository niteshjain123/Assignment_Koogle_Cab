package com.assignment.tv9.network

import com.assignment.tv9.model.Carousel.CarouselList
import com.assignment.tv9.model.Dashboard.DashboardList
import com.assignment.tv9.model.Details.DetailsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url




interface API {
    @GET("v2/homepage-top-slider-new")
    suspend fun getCarousel(): Response<CarouselList>

    @GET("v1/show-episode-up-next")
    suspend fun getDashboardList() : Response<List<DashboardList>>

    @GET("v1/post-details-by-id")
    suspend fun getDetailsPage(@Query("id") article_id:String) : Response<List<DetailsData>>

    @GET
    suspend fun getDetailsPageApi1(@Url url:String ): Response<DetailsData>

    /*https://www.tv9hindi.com/wp-json/wp/v2/posts/1828503?token=saaKkv5v@eW7F$YKTbROu3e7
    https://www.tv9hindi.com/wp-json/wp/v2/posts/1828517?token=saaKkv5v@eW7F$YKTbROu3e7*/

    @GET
    suspend fun getDetailsPageApi2(@Url url:String ): Response<DetailsData>

}