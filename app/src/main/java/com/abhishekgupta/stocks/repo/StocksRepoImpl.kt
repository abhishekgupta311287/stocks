package com.abhishekgupta.stocks.repo

import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.repo.db.IStocksHistoryDao
import com.abhishekgupta.stocks.repo.network.IStocksApi
import retrofit2.HttpException

class StocksRepoImpl(
    private val api: IStocksApi,
    private val dao: IStocksHistoryDao
) : IStocksRepo {

    override suspend fun getStockQuotes(sidsList: List<String>, isPolling: Boolean): Quote {
        return try {
            var sids = ""

            sidsList.forEach { sid ->
                sids += "$sid,"
            }

            val quote = api.getStockQuotes(sids)

            if (isPolling && quote.success && quote.data?.isNotEmpty() == true) {
                dao.insertStocks(quote.data)
            }
            quote
        } catch (e: HttpException) {
            Quote(false, null, "Http Error - ${e.code()}", null)
        }
    }

    override suspend fun getStockHistory(stock: Stock): List<Stock> {
        val list = dao.getStockHistory(stock.sid).reversed()

        return if (list.isEmpty()) {
            listOf(stock)
        } else {
            val arrayList = ArrayList(list)
            arrayList.removeAt(arrayList.size - 1)
            arrayList.add(stock)
            arrayList
        }

    }

    override suspend fun deleteAll() = dao.deleteAll()
}