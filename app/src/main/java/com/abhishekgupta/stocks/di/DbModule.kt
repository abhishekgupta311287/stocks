package com.abhishekgupta.stocks.di

import androidx.room.Room
import com.abhishekgupta.stocks.repo.db.IStocksHistoryDao
import com.abhishekgupta.stocks.repo.db.StocksHistoryDaoImpl
import com.abhishekgupta.stocks.repo.db.StocksHistoryDb
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(get(), StocksHistoryDb::class.java, "stocks_history.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<IStocksHistoryDao> { StocksHistoryDaoImpl((get() as StocksHistoryDb).stocksHistoryDao()) }
}