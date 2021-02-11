package com.abhishekgupta.stocks.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhishekgupta.stocks.di.appModule
import com.abhishekgupta.stocks.di.dbModule
import com.abhishekgupta.stocks.di.networkModule
import com.abhishekgupta.stocks.model.Quote
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.repo.IStocksRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
class StocksViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repo = mockk<IStocksRepo>(relaxed = true)

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

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            listOf(dbModule, networkModule, appModule)
        }

    }

    @After
    fun close() {
        stopKoin()
    }

    @Test
    fun `verify quotes api success`() = runBlockingTest {
        val viewModel = StocksViewModel(repo)

        coEvery {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), false
            )
        }.returns(quote)

        viewModel.getStockQuotes()

        val resource = viewModel.stocksLiveData.value
        assert(resource is Resource.Success)

        val data = resource?.data

        Assert.assertEquals(1, data?.size)
        Assert.assertEquals("TCS", data!![0].sid)

    }

    @Test
    fun `verify quotes api failure`() = runBlockingTest {
        val viewModel = StocksViewModel(repo)

        coEvery {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), false
            )
        }.returns(quote.copy(success = false, error = "error", errorType = "test"))

        viewModel.getStockQuotes()

        val resource = viewModel.stocksLiveData.value
        assert(resource is Resource.Error)

        Assert.assertEquals("error - test", resource?.message)

    }

    @Test
    fun `verify quotes api success with empty list`() = runBlockingTest {
        val viewModel = StocksViewModel(repo)

        coEvery {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), false
            )
        }.returns(quote.copy(data = emptyList()))

        viewModel.getStockQuotes()

        val resource = viewModel.stocksLiveData.value
        assert(resource is Resource.Error)

    }

    @Test
    fun `verify start and stop polling of quotes`() = runBlocking {

        val viewModel = StocksViewModel(repo)

        coEvery {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), true
            )
        }.returns(quote)

        Assert.assertFalse(viewModel.isPolling)

        viewModel.pollStockQuotes()

        delay(StocksViewModel.POLL_INTERVAL)

        Assert.assertTrue(viewModel.isPolling)

        coVerify(atLeast = 2) {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), true
            )
        }

        viewModel.stopPolling()
        Assert.assertFalse(viewModel.isPolling)
        coVerify(atMost = 2) {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), true
            )
        }
    }

    @Test
    fun `verify stocks history`() = runBlockingTest {
        val viewModel = StocksViewModel(repo)

        coEvery { repo.getStockHistory(stock) }.returns(stocks)

        viewModel.fetchStockHistory(stock)

        coVerify { repo.getStockHistory(stock) }

        var list = viewModel.historyLiveData.value

        Assert.assertEquals(1, list?.size)

        coEvery { repo.getStockHistory(stock) }.returns(emptyList())

        viewModel.fetchStockHistory(stock)

        list = viewModel.historyLiveData.value

        Assert.assertEquals(0, list?.size)
    }

    @Test
    fun `verify expensive stock history`() = runBlockingTest {
        val viewModel = StocksViewModel(repo)
        val stock2 = stock.copy(sid = "RELI", price = 4000.0f)
        coEvery {
            repo.getStockQuotes(
                listOf(
                    "RELI",
                    "TCS",
                    "ITC",
                    "HDBK",
                    "INFY"
                ), false
            )
        }.returns(
            quote.copy(
                data = listOf(
                    stock,
                    stock2
                )
            )
        )

        viewModel.getStockQuotes()

        viewModel.fetchExpensiveStockHistory()

        coVerify { repo.getStockHistory(stock2) }

    }

    @Test
    fun `verify repo delete call in view model init`() = runBlockingTest {
        StocksViewModel(repo)

        coVerify { repo.deleteAll() }
    }
}