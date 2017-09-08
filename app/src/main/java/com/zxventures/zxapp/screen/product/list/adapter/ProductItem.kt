package com.zxventures.zxapp.screen.product.list.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zxventures.zxapp.R
import com.zxventures.zxapp.extensions.toCurrency
import com.zxventures.zxapp.model.Product
import kotlinx.android.synthetic.main.product_item.view.*
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
class ProductItem(itemView: View, private val itemClick: (Product) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: Product) {
        Glide.with(itemView.context)
                .load(data.imageUrl)
                .apply(RequestOptions.errorOf(R.drawable.no_image))
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {


                        itemView.progress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?,
                                                 model: Any?,
                                                 target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {

                        itemView.progress.visibility = View.GONE
                        return false
                    }

                }).into(itemView.imgProduct)

        itemView.txtProductItemPrice.text = data.price?.toCurrency(Locale.getDefault(),
                Currency.getInstance(Locale("pt","BR")))
        itemView.txtProductItemTitle.text = data.title

        itemView.setOnClickListener { itemClick(data) }
    }
}