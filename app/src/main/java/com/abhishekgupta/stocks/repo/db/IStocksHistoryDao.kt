package com.abhishekgupta.stocks.repo.db

import com.abhishekgupta.stocks.model.Stock

interface IStocksHistoryDao {

    suspend fun getStockHistory(sid: String): List<Stock>

    suspend fun insertStocks(stocks: List<Stock>)

    suspend fun deleteAll()

}