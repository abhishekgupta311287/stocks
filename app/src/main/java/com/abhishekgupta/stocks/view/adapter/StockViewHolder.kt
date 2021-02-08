package com.abhishekgupta.stocks.view.adapter

import android.view.View
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksItemLayoutBinding
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.view.viewholder.BaseViewHolder

class StockViewHolder(view: View) : BaseViewHolder<Stock>(view) {

    override fun bind(t: Stock) {
        val binding = StocksItemLayoutBinding.bind(itemView)
        binding.sid.text = t.sid
        binding.price.text = itemView.resources.getString(R.string.sid_price, t.price.toString())

        binding.changeIcon.setImageResource(
            if (t.change > 0) {
                R.drawable.green_stock_up_icon
            } else {
                R.drawable.red_stock_down_icon
            }
        )
    }
}