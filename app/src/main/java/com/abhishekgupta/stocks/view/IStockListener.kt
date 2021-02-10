package com.abhishekgupta.stocks.view

import com.abhishekgupta.stocks.model.Stock


interface IStockListener {
    fun onStockSelected(stock:Stock)
}