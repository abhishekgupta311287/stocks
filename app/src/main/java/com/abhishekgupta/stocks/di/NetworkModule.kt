package com.abhishekgupta.stocks.di

import com.abhishekgupta.stocks.BuildConfig
import com.abhishekgupta.stocks.repo.network.IStocksApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        val okHttpClient = builder.build()
        okHttpClient
    }

    single {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.tickertape.in")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(IStocksApi::class.java)
        api
    }
}