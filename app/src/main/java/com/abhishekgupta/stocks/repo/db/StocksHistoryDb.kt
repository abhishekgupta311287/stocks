package com.abhishekgupta.stocks.repo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abhishekgupta.stocks.model.Stock

@Database(entities = [Stock::class], version = 1, exportSchema = false)
abstract class StocksHistoryDb : RoomDatabase() {

    abstract fun stocksHistoryDao(): IStocksHistoryDBDao
}