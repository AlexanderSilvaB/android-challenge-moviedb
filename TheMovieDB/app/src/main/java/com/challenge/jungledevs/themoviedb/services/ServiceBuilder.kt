package com.challenge.jungledevs.themoviedb.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    const val URL = "https://api.themoviedb.org/"
    const val API_KEY = "eb85023d273ca8bbd07844e395be1139"


    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttp = OkHttpClient.Builder().addInterceptor { chain ->
        val url = chain
            .request()
            .url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }.addInterceptor(interceptor)

    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService(serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }
}