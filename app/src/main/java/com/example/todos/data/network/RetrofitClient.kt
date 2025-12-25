package com.example.todos.data.network

import android.content.Context
import com.github.tony19.logback.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://hive.mrdekk.ru/todo/"

class RetrofitClient(context: Context) {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Generate-Fails", "0")
                .build()
            chain.proceed(request)
        }
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}