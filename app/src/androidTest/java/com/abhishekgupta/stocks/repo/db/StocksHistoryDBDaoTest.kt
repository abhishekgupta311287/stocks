package com.abhishekgupta.stocks.repo.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.abhishekgupta.stocks.model.Stock
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StocksHistoryDBDaoTest {

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

    private lateinit var db: StocksHistoryDb
    private lateinit var dao: IStocksHistoryDBDao

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(appContext, StocksHistoryDb::class.java)
            .fallbackToDestructiveMigration()
            .build()
        dao = db.stocksHistoryDao()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() = runBlocking {
        dao.deleteAll()
        db.close()
    }

    @Test
    fun insert() = runBlocking {
        db.stocksHistoryDao().insert(stock)

        val list = dao.getStockHistory(stock.sid)

        Assert.assertEquals(1, list.size)

        val s = list[0]

        Assert.assertEquals(s.id, 1)
        Assert.assertEquals(s.sid, stock.sid)
        Assert.assertEquals(s.price, stock.price)
        Assert.assertEquals(s.change, stock.change)
        Assert.assertEquals(s.high, stock.high)
        Assert.assertEquals(s.low, stock.low)
        Assert.assertEquals(s.close, stock.close)
        Assert.assertEquals(s.volume, stock.volume)
        Assert.assertEquals(s.date, stock.date)
    }

    @Test
    fun insertStocks() = runBlocking {
        db.stocksHistoryDao().insertStocks(stocks)

        val list = dao.getStockHistory(stock.sid)

        Assert.assertEquals(1, list.size)
    }

    @Test
    fun deleteAll() = runBlocking {
        db.stocksHistoryDao().insertStocks(stocks)

        var list = dao.getStockHistory(stock.sid)

        Assert.assertEquals(1, list.size)

        dao.deleteAll()

        list = dao.getStockHistory(stock.sid)

        Assert.assertEquals(0, list.size)
    }

}