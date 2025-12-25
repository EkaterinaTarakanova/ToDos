package com.example.todos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class YandexOAuthActivity : Activity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    url: String?
                ): Boolean {
                    url?.let { currentUrl ->
                        Log.d("YandexOAuth", "Processing URL: $currentUrl")

                        if (currentUrl.contains("access_token=")) {
                            extractTokenFromUrl(currentUrl)
                            return true
                        }
                    }
                    return false
                }
            }
        }
        val authUrl = "https://oauth.yandex.ru/authorize?" +
                "response_type=token" +
                "&client_id=${BuildConfig.YANDEX_CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.YANDEX_REDIRECT_URI}" +
                "&force_confirm=true" +
                "&prompt=login"

        setContentView(webView)
        webView.loadUrl(authUrl)
    }

    private fun extractTokenFromUrl(url: String) {
        val token = try {
            if (url.contains("#")) {
                url.substringAfter("#").substringAfter("access_token=").substringBefore("&")
            } else {
                url.substringAfter("?").substringAfter("access_token=").substringBefore("&")
            }
        } catch (e: Exception) {
            Log.e("YandexOAuth", "${e.message}")
            null
        }

        if (!token.isNullOrEmpty()) {
            val result = Intent().apply {
                putExtra("TOKEN", token)
            }
            setResult(RESULT_OK, result)
        } else {
            setResult(RESULT_CANCELED)
        }

        finish()
    }
}