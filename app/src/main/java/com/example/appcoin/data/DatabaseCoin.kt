package com.example.appcoin.data

import android.provider.BaseColumns

object DatabaseCoin {

    object CoinEntry : BaseColumns{

        const val TABLE_NAME = "monedas"

        const val COLUMN_COINNAME = "coin"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_YEAR = "year"
    }
}