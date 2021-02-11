package com.abhishekgupta.stocks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks_history")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val sid: String,
    val price: Float,
    val close: Float,
    val change: Float,
    val high: Float,
    val low: Float,
    val volume: Long,
    val date: String
)
