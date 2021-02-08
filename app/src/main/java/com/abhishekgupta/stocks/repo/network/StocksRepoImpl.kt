package com.abhishekgupta.stocks.repo.network

import com.abhishekgupta.stocks.model.Quote

class StocksRepoImpl(private val api: IStocksApi) : IStocksRepo {

    override suspend fun getStockQuotes(sidsList: List<String>): Quote {
        var sids = ""

        sidsList.forEach { sid ->
            sids += "$sid,"
        }

        return api.getStockQuotes(sids)
    }
}