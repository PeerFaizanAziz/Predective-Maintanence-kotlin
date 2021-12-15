package com.example.predictivemaintenence

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientt {
    private val logger: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okhttp: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(logger)

    val instance: ApiInterface by lazy {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8082/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp.build())
            .build()

        retrofitBuilder.create(ApiInterface::class.java)

    }

}