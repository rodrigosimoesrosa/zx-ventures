package com.zxventures.zxapp.screen.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.zxventures.zxapp.R
import com.zxventures.zxapp.model.PointOfContactData
import kotlinx.android.synthetic.main.address_row.view.*

/**
 * Created by rodrigosimoesrosa
 */
class PointOfContactItem(itemView: View, private val itemClick: (PointOfContactData.PointOfContact) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: PointOfContactData.PointOfContact) {
        val status = when(data.status){
            PointOfContactData.StatusPoint.AVAILABLE -> itemView.context.getString(R.string.available)
            PointOfContactData.StatusPoint.CLOSED -> itemView.context.getString(R.string.closed)
        }
        itemView.txtAddress.text = "${data.address?.address1}, ${data.address?.number} - ${data.tradingName} - $status}"
        itemView.setOnClickListener { itemClick(data) }
    }
}