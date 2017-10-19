package com.zxventures.zxapp.screen.home.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zxventures.zxapp.R
import com.zxventures.zxapp.model.PointOfContactData

/**
 * Created by rodrigosimoesrosa
 */
class PointOfContactAdapter(var context: Context, var lists: MutableList<PointOfContactData.PointOfContact>, private val itemClick: (PointOfContactData.PointOfContact) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).
                inflate(R.layout.address_row, parent, false)
        return PointOfContactItem(v, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as PointOfContactItem).bind(lists[position])
    }
}

