# Stocks Real Time
=============================

This app displays real time stock prices .

Introduction
--------------------
The app consists of two fragment (StockFragment and HistoryFragment) .
StockFragment displays the list of stocks along with current price and change.
It also provide app bar menu button to display history of most expensive stock and a play button to start display real time stock price changes.

HistoryFragment display the price of the stock selected on stocks screen and chart graph history for that stock
If play button is never used, the chart remains empty

### Functionality
##### Fetch real time stock prices and required data from /stocks/quotes api using retrofit
##### Stores fetched stock prices in Room Db if stock polling was enabled using play button
##### Displays history chart of the stocks in list
##### Displays history chart of the most expensive stock in list when history menu button is used

### Architecture Used
MVVM with LiveData

### UI Tests
Not added because of an issue in shared view model from koin.
UI test can added using espresso and mocking view model.

### Database Instrumentation Tests
The project creates an in memory database for each database test and
runs them on the device.

### Local Unit Tests
#### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.

#### Repository Tests
Each Repository is tested using local unit tests with mock retrofit and
mock database.

#### Webservice Tests
The project uses MockWebServer project to test REST api interactions.

#### DB Tests
StocksHistoryDaoImpl is tested for integration test with mock dao implementations.

### Libraries
* Android Support Library
* Android Architecture Components
* Facebook Shimmer Library for shimmer loading effect
* Koin for dependency injection
* Retrofit and OkHttp for REST api communication
* Kotline coroutines
* Mockwebserver for api test
* Mockk for mocking in tests
* Room for offline storage



