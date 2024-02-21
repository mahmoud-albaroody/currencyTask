package com.bitaqaty.currencyapp.data.remote.dto

data class Query(
    val amount: Double,
    val from: String,
    val to: String
)