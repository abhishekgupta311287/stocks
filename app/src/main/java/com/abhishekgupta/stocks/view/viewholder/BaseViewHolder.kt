package com.abhishekgupta.stocks.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(t: T)
}