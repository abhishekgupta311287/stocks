package com.abhishekgupta.stocks.model

data class Stock(
    val sid: String,
    val price: Float,
    val close: Float,
    val change: Float,
    val high: Float,
    val low: Float,
    val volume: Float,
    val date: String
)
