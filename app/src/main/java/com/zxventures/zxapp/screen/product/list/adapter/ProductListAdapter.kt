package com.zxventures.zxapp.screen.product.list.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zxventures.zxapp.R
import com.zxventures.zxapp.model.Product

/**
 * Created by rodrigosimoesrosa
 */
class ProductListAdapter(var context: Context, var lists: List<Product>, private val itemClick: (Product) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return ProductItem(LayoutInflater.from(context).
                inflate(R.layout.product_item, parent, false), itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ProductItem).bind(lists[position])
    }
}