package com.abhishekgupta.stocks.repo

import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.repo.network.IStocksApi

class StocksRepoImpl(private val api: IStocksApi) : IStocksRepo {

    override suspend fun getStockQuotes(sidsList: List<String>): Quote {
        var sids = ""

        sidsList.forEach { sid ->
            sids += "$sid,"
        }

        return api.getStockQuotes(sids)
    }
}