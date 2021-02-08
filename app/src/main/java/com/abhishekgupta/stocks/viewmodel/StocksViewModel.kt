package com.abhishekgupta.stocks.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.repo.IStocksRepo
import kotlinx.coroutines.launch

class StocksViewModel(private val repo: IStocksRepo) : ViewModel() {
    val stocksLiveData: MutableLiveData<List<Stock>> = MutableLiveData()

    fun getStockQuotes() {
        viewModelScope.launch {
            Resource.Loading(null)
            val (success, data, error, errorType) = repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                )
            )

            if (success && data?.isNotEmpty() == true) {
                Resource.Success(data)
            } else {
                Resource.Error("$error - $errorType", data)
            }

        }
    }

    fun listenToStockQuotes() {
        // TODO : implementation for play button
    }
}