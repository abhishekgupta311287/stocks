package com.abhishekgupta.stocks.repo.db

import androidx.room.*
import com.abhishekgupta.stocks.model.Stock

@Dao
interface IStocksHistoryDBDao {

    @Query("SELECT * FROM stocks_history where sid = :sid order by id desc limit 10")
    suspend fun getStockHistory(sid:String): List<Stock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: Stock)

    @Transaction
    suspend fun insertStocks(stocks: List<Stock>) {
        stocks.forEach {
            insert(it)
        }
    }

    @Query("DELETE FROM stocks_history")
    suspend fun deleteAll()

}