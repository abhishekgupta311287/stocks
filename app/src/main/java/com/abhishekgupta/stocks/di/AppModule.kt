package com.abhishekgupta.stocks.di

import com.abhishekgupta.stocks.repo.IStocksRepo
import com.abhishekgupta.stocks.repo.StocksRepoImpl
import com.abhishekgupta.stocks.viewmodel.StocksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IStocksRepo> { StocksRepoImpl(get(), get()) }
    viewModel { StocksViewModel(get()) }
}