package com.abhishekgupta.stocks.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.repo.IStocksRepo
import kotlinx.coroutines.launch
import java.util.*

class StocksViewModel(private val repo: IStocksRepo) : ViewModel() {

    init {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }

    var isPolling: Boolean = false
    val stocksLiveData: MutableLiveData<Resource<List<Stock>>> = MutableLiveData()
    val historyLiveData: MutableLiveData<List<Stock>> = MutableLiveData()

    private var timer: Timer? = null

    fun getStockQuotes() {
        viewModelScope.launch {
            stocksLiveData.value = Resource.Loading(null)
            val (success, data, error, errorType) = repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ),
                isPolling
            )

            if (success && data?.isNotEmpty() == true) {
                stocksLiveData.value = Resource.Success(data)
            } else {
                stocksLiveData.value = Resource.Error("$error - $errorType", data)
            }

        }
    }

    fun pollStockQuotes() {
        isPolling = true
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getStockQuotes()
            }
        }, 0, POLL_INTERVAL)

    }

    fun stopPolling() {
        timer?.cancel()
        isPolling = false
    }

    fun fetchStockHistory(stock: Stock) {
        viewModelScope.launch {
            historyLiveData.value = repo.getStockHistory(stock)
        }
    }

    companion object {
        private const val POLL_INTERVAL = 5000L // in seconds
    }
}