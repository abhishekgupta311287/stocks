package com.abhishekgupta.stocks.repo.network

import com.abhishekgupta.stocks.model.Quote
import retrofit2.http.GET
import retrofit2.http.Query

interface IStocksApi {
    @GET("/stocks/quotes")
    suspend fun getStockQuotes(@Query("sids") sids: String): Quote
}