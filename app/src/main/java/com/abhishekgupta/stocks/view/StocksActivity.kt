package com.abhishekgupta.stocks.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksActivityBinding

class StocksActivity : AppCompatActivity() {

    private lateinit var binding: StocksActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StocksActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StocksFragment.newInstance())
                    .commitNow()
        }
    }
}