package com.abhishekgupta.stocks.repo.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.model.Stock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StocksHistoryDaoImplTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val dbDao = mockk<IStocksHistoryDBDao>(relaxed = true)

    private val stock = Stock(
        null,
        "TCS",
        3213.3f,
        3213.3f,
        32.3f,
        3218.3f,
        3210.3f,
        32133,
        "2021-02-10T10:27:15.000Z"
    )
    private val stocks = listOf(stock)

    private val quote = Quote(
        true,
        stocks,
        null,
        null
    )

    @Test
    fun `verify get stock history`() = runBlockingTest {
        val dao = StocksHistoryDaoImpl(dbDao)

        dao.getStockHistory("TCS")

        coVerify { dbDao.getStockHistory("TCS") }

    }

    @Test
    fun `verify insert Stocks`() = runBlockingTest {
        val dao = StocksHistoryDaoImpl(dbDao)

        dao.insertStocks(stocks)

        coVerify { dbDao.insertStocks(stocks) }
    }

    @Test
    fun `verify delete all`() = runBlockingTest {

        val dao = StocksHistoryDaoImpl(dbDao)

        dao.deleteAll()

        coVerify { dbDao.deleteAll() }
    }
}