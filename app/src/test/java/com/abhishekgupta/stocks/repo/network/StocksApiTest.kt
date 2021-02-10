package com.abhishekgupta.stocks.repo.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStreamReader

class StocksApiTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var api: IStocksApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(IStocksApi::class.java)

    }

    @Test
    fun `stocks quote success`() = runBlocking {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(readStringFromFile("success.json"))
            }
        }
        val quote = api.getStockQuotes("TCS,RELI")
        val request = mockWebServer.takeRequest()

        Assert.assertThat(request.path, CoreMatchers.`is`("/stocks/quotes?sids=TCS%2CRELI"))

        Assert.assertThat(quote.success, CoreMatchers.`is`(true))
        Assert.assertThat(quote.error, CoreMatchers.nullValue())
        Assert.assertThat(quote.errorType, CoreMatchers.nullValue())
        Assert.assertThat(quote.data?.size, CoreMatchers.`is`(2))

        val stock = quote.data!![0]

        Assert.assertThat(stock.id, CoreMatchers.nullValue())
        Assert.assertThat(stock.sid, CoreMatchers.`is`("TCS"))
        Assert.assertThat(stock.price, CoreMatchers.`is`(3213.3f))
        Assert.assertThat(stock.change, CoreMatchers.`is`(36.4f))
        Assert.assertThat(stock.close, CoreMatchers.`is`(3176.9f))
        Assert.assertThat(stock.high, CoreMatchers.`is`(3219.4f))
        Assert.assertThat(stock.low, CoreMatchers.`is`(3157.3f))
        Assert.assertThat(stock.volume, CoreMatchers.`is`(2127983))
        Assert.assertThat(stock.date, CoreMatchers.`is`("2021-02-10T10:27:15.000Z"))

    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    private fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }

}