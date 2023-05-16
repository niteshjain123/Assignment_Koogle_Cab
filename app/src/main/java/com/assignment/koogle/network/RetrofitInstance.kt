package com.assignment.koogle.network

import com.assignment.koogle.util.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {

        private val retrofitApiBuild by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2IiwianRpIjoiMzViYTg5ZWVlZDczNjdiNTc4MDczMGYxZWViYjljOTliYjRjYzc5ODY4NGViNzJjN2NmOTdkN2Q3ZDM1MzdkYjkxYWU3NDMzM2I4MTcxYTgiLCJpYXQiOjE2ODM4NjQwOTkuNzE1NTI3LCJuYmYiOjE2ODM4NjQwOTkuNzE1NTMsImV4cCI6MTcxNTQ4NjQ5OS43MTMyMDEsInN1YiI6IjE0Iiwic2NvcGVzIjpbXX0.E_rypngq-gHFS8gnskgoCEMfMzvlix8cXEwg5rztjpB6TYf1svQyaR4BnfDXg8zrzYz3kvn_hsGSCYpVnt7XxgMdOk51Bg_19JRnvdJyRfTIYl-Eze6h7WVMPVs8NG-Rqugr-FMhiHht5Wu8QbpOeuU105k74ZpFLi2grmyO4U94cQuuQXsh3hcys1FEZaqI34RxAYxCGTmseusWEF3RZE-VtyBmC9mDIQnhYoLRig4_G16h9QmMmE2WvnxnEnqSrI0CT5kkTczQPkP0wEGw1KvSWcUJGyuxNZ1pIayazWCe78Djb8TKlycHKZPPbZ_o7tFOkPie8UYPkBERWbwsP73OouwN7hvv-HS1jZ6d8MU_GuuqlnklPJwE0NTF93wW5-j61jPm-CW-qW2YXv7bFpXcuhDJ4NvKdclgVBUrZ-UaZlJx4-fYM9DNNJg6905nAERxTDRCx5T144IfiiCC16ASP75xiAYEqzr-swiTkHLQ84O-DNDEq53Zm8QEb8pw8_guDRGEwmoBKz3vKogWCg3rT6VyknZmkIZ92u4SiZI9jngf2XXULApFsUp6QFRgK2lFXKSTB_3QcZSfLgUQI2neg7gwsO4fdjOr1EHA6mkOlPBgqGA3iTluhqTqA267ZUZbC-us8hebcbrObdbBEoKne5yJjOyYJNmgLboKUeg")
                    .build()
                chain.proceed(newRequest)
            }).build()

            Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val koogleApi by lazy {
            retrofitApiBuild.create(API::class.java)
        }
    }
}