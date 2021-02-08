package com.abhishekgupta.stocks.repo

import com.abhishekgupta.stocks.model.Quote

interface IStocksRepo {
    suspend fun getStockQuotes(sidsList: List<String>): Quote
}