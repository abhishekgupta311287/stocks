package com.abhishekgupta.stocks.repo

import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.model.Stock

interface IStocksRepo {
    suspend fun getStockQuotes(sidsList: List<String>, isPolling:Boolean): Quote

    suspend fun getStockHistory(stock: Stock): List<Stock>

    suspend fun deleteAll()
}