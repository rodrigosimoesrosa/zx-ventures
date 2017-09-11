package com.zxventures.zxapp.screen.product

import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.zxventures.zxapp.R
import com.zxventures.zxapp.base.mvp.BaseMVPActivity
import com.zxventures.zxapp.extensions.isNetworkConnected
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.presenter.product.contract.ProductContract
import com.zxventures.zxapp.presenter.product.impl.ProductCategoryPresenterImpl
import com.zxventures.zxapp.screen.product.list.adapter.ProductAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.product_content.*

/**
 * Created by rodrigosimoesrosa
 */
class ProductActivity : BaseMVPActivity<ProductContract.ProductCategoryView, ProductContract.ProductCategoryPresenter>(),
        ProductContract.ProductCategoryView {

    override var presenter: ProductContract.ProductCategoryPresenter = ProductCategoryPresenterImpl()

    override fun getLayout(): Int = R.layout.activity_product

    private var adapterProduct: ProductAdapter? = null
    private val ANIMATION_DURATION = 300L
    private val ANIMATION_DELAY_ITEM = 100L

    private val list : MutableList<Category> = mutableListOf()
    private lateinit var pointOfContact: PointOfContactData.PointOfContact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pointOfContact = (savedInstanceState?.getSerializable(
                PointOfContactData.PointOfContact.POINT_OF_CONTACT)?:
                getParcelable<PointOfContactData.PointOfContact>(
                        PointOfContactData.PointOfContact.POINT_OF_CONTACT))
                as PointOfContactData.PointOfContact

        setSupportActionBar(toolbar)
        supportActionBar?.title = pointOfContact.tradingName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buildAdapter()
        presenter.loadCategorys()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(PointOfContactData.PointOfContact.POINT_OF_CONTACT, pointOfContact)
        super.onSaveInstanceState(outState)
    }

    private fun buildAdapter(){
        adapterProduct = ProductAdapter(list, pointOfContact, supportFragmentManager)
        viewPager.adapter = adapterProduct
        viewPager.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                viewPager.viewTreeObserver.removeOnPreDrawListener(this)
                for (i in 0..(viewPager.childCount - 1)) {
                    val v = viewPager.getChildAt(i)
                    v.alpha = 0.0f
                    v.animate().alpha(1.0f)
                            .setDuration(ANIMATION_DURATION)
                            .setInterpolator(FastOutSlowInInterpolator())
                            .setStartDelay(i * ANIMATION_DELAY_ITEM)
                            .start()
                }
                return true
            }
        })
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun showCategorys(categorys: MutableList<Category>) {
        this.list.clear()
        progressBar?.visibility = View.GONE

        this.list.addAll(categorys)
        adapterProduct?.notifyDataSetChanged()
    }

    override fun showEmptyState(msg:String) {
        progressBar?.visibility = View.GONE
        txtAlert?.visibility = View.VISIBLE

        txtAlert?.text = msg
    }

    override fun showError(e: Exception) {
        if(isNetworkConnected()){
            val msg = when(e){
                is ApolloNetworkException -> getString(R.string.problem_connection)
                is ApolloHttpException -> getString(R.string.problem_connection)
                else -> getString(R.string.generic_error)
            }
            showSnack(container, msg)
            showEmptyState(getString(R.string.category_empty_data))
        }else{
            showSnack(container, getString(R.string.no_internet_connection))
            showEmptyState(getString(R.string.no_internet_connection))
        }
    }

    override fun onDestroy() {
        presenter.cancel()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
