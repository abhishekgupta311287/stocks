package com.abhishekgupta.stocks.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.repo.db.IStocksHistoryDao
import com.abhishekgupta.stocks.repo.network.IStocksApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class StocksRepoImplTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val api = mockk<IStocksApi>(relaxed = true)
    private val dao = mockk<IStocksHistoryDao>(relaxed = true)

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
    fun `verify stocks api call when not polling`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)
        val sids = listOf("TCS")

        coEvery { api.getStockQuotes("TCS,") }.returns(quote)
        repo.getStockQuotes(sids, false)

        coVerify { api.getStockQuotes("TCS,") }
        coVerify(atMost = 0, atLeast = 0) { dao.insertStocks(quote.data!!) }


    }

    @Test
    fun `verify stocks api call and db insert when polling`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)
        val sids = listOf("TCS")

        coEvery { api.getStockQuotes("TCS,") }.returns(quote)
        repo.getStockQuotes(sids, true)

        coVerify { api.getStockQuotes("TCS,") }
        coVerify(atMost = 1) { dao.insertStocks(quote.data!!) }

    }

    @Test
    fun `verify http error`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)
        val sids = listOf("TCS")
        val response: Response<Quote> = Response.error(401, EMPTY_RESPONSE)
        coEvery { api.getStockQuotes("TCS,") }.throws(HttpException(response))
        val (success, data, error, errorType) = repo.getStockQuotes(sids, true)

        coVerify { api.getStockQuotes("TCS,") }
        coVerify(atMost = 0, atLeast = 0) { dao.insertStocks(quote.data!!) }

        assertFalse(success)
        assertNull(data)
        assertEquals("Http Error - 401", error)
        assertNull(errorType)
    }

    @Test
    fun `verify stock history`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)

        coEvery { dao.getStockHistory(stock.sid) }.returns(stocks)

        val list = repo.getStockHistory(stock)

        assert(list.contains(stock))

    }

    @Test
    fun `verify stock history when no history in db`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)

        coEvery { dao.getStockHistory(stock.sid) }.returns(emptyList())

        val list = repo.getStockHistory(stock)

        assert(list.contains(stock))

    }

    @Test
    fun `verify delete all`() = runBlockingTest {
        val repo = StocksRepoImpl(api, dao)

        repo.deleteAll()

        coVerify { dao.deleteAll() }
    }
}