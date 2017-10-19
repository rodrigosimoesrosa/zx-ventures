package com.zxventures.zxapp.screen.product.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zxventures.zxapp.R
import com.zxventures.zxapp.base.BaseActivity
import com.zxventures.zxapp.extensions.toCurrency
import com.zxventures.zxapp.model.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.product_detail_content.*
import java.util.*

class ProductDetailActivity : BaseActivity() {

    override fun getLayout(): Int = R.layout.activity_product_detail

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = (savedInstanceState?.getSerializable(Product.PRODUCT)?: getSerializable()) as Product

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.product_detail_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        build()
    }

    fun build(){
        txtProductDetailTitle.text = product.title

        Glide.with(this)
                .load(product.imageUrl)
                .apply(RequestOptions.errorOf(R.drawable.no_image))
                .listener(object : RequestListener<Drawable>{
                    override fun onResourceReady(resource: Drawable?, model: Any?,
                                                 target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }
                }).into(imgProduct)

        txtProductDetailSubtitle.text = product.price?.toCurrency(Locale.getDefault(),
                Currency.getInstance(Locale("pt","BR")))

        txtProductDetailDescription.text = product.description?:
                getString(R.string.product_detail_without_description)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(Product.PRODUCT, product)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}


