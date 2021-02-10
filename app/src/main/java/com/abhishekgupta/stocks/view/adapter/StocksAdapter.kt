package com.abhishekgupta.stocks.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.view.IStockListener
import com.abhishekgupta.stocks.view.viewholder.BaseViewHolder

class StocksAdapter(private val listener: IStockListener) : RecyclerView.Adapter<BaseViewHolder<Stock>>() {
    var stocks: List<Stock> = ArrayList()
        set(value) {
            field = value
            notifyItemRangeChanged(0, field.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Stock> {
        val view = View.inflate(parent.context, R.layout.stocks_item_layout, null)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Stock>, position: Int) {
        val stock = stocks[position]
        holder.bind(stock)
        holder.itemView.setOnClickListener {
            listener.onStockSelected(stock)
        }
    }

    override fun getItemCount() = stocks.size
}