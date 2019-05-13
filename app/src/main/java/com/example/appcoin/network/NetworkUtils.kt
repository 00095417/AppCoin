package com.example.appcoin.network

import android.net.Uri
import java.net.URL

class NetworkUtils {

    companion object{
        val COIN_API = "https://restapicoin.herokuapp.com/"
        val PATH_COIN = "coin"

        fun buildtUrl() = URL (
            Uri.parse(COIN_API)
                .buildUpon()
                .build().toString()
        )

        fun buildURL(id: String) = URL(
            Uri.parse(COIN_API)
                .buildUpon()
                .appendPath(PATH_COIN)
                .appendPath(id)
                .build().toString()
        )

        fun getHTTPResult(url: URL) = url.readText()
    }
}