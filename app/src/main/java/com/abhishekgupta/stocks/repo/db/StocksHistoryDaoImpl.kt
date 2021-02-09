package com.abhishekgupta.stocks.repo.db

import com.abhishekgupta.stocks.model.Stock

class StocksHistoryDaoImpl(private val dao: IStocksHistoryDBDao) : IStocksHistoryDao {
    override suspend fun getStockHistory(sid: String): List<Stock> = dao.getStockHistory(sid)

    override suspend fun insertStocks(stocks: List<Stock>) = dao.insertStocks(stocks)

    override suspend fun deleteAll() = dao.deleteAll()
}