package com.abhishekgupta.stocks.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksActivityBinding
import com.abhishekgupta.stocks.viewmodel.StocksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StocksActivity : AppCompatActivity() {

    private lateinit var binding: StocksActivityBinding
    private val viewModel: StocksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StocksActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StocksFragment.newInstance())
                    .commit()
        }

        viewModel.historyLiveData.observe(this, {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HistoryFragment.newInstance())
                .addToBackStack("HistoryFragment")
                .commit()
        })
    }
}