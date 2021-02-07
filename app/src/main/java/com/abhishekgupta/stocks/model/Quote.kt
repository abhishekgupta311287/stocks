package com.abhishekgupta.stocks.model

data class Quote(
    val success: Boolean,
    val data: List<Stock>
)
