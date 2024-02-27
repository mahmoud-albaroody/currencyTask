package com.bitaqaty.currencyapp.data.remote.dto

import java.io.Serializable

data class CurrencyRate(
    val currencyName: String,
    val currencyValue: String
): Serializable