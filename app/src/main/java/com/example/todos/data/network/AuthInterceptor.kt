package com.example.todos.data.network

import android.content.Context
import com.example.todos.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val oauthToken = prefs.getString("oauth_token", null)
        val requestBuilder = chain.request().newBuilder()

        when {
            !oauthToken.isNullOrEmpty() -> {
                requestBuilder.addHeader("Authorization", "OAuth $oauthToken")
            }
            BuildConfig.BEARER_TOKEN.isNotEmpty() -> {
                requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}