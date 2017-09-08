package com.zxventures.zxapp.screen.product.list

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.base.mvp.BaseMVPFragment
import com.zxventures.zxapp.extensions.isNetworkConnected
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.model.Product
import com.zxventures.zxapp.screen.product.ProductActivity
import com.zxventures.zxapp.screen.product.ProductContract
import com.zxventures.zxapp.screen.product.detail.ProductDetailActivity
import com.zxventures.zxapp.screen.product.list.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_product_list.*

/**
 * Created by rodrigosimoesrosa
 */
class ProductListFragment : BaseMVPFragment<ProductContract.ProductListView,
        ProductContract.ProductListPresenter>(), ProductContract.ProductListView {

    companion object {

        fun build(category: Category, pointOfContact: PointOfContactData.PointOfContact) : ProductListFragment {
            val fragment: ProductListFragment = ProductListFragment::class.java.newInstance()
            fragment.setData(category, Category.CATEGORY)
            fragment.setData(pointOfContact, PointOfContactData.PointOfContact.POINT_OF_CONTACT)
            return fragment
        }
    }

    private val COLUMNS = 2
    private val SPACE_BETWEEN_ITEMS = 10
    private val ANIMATION_DURATION = 300L
    private val ANIMATION_DELAY_ITEM = 100L

    private val products: MutableList<Product> = mutableListOf()
    private var category: Category? = null
    private var pointOfContact: PointOfContactData.PointOfContact? = null
    private var adapter: ProductListAdapter? = null

    override var presenter: ProductContract.ProductListPresenter = ProductContract.ProductListPresenterImpl()

    override fun getLayout(): Int = R.layout.fragment_product_list

    override fun getName(): String {
        return activity.getString(R.string.product_section_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = (savedInstanceState?.getSerializable(
                Category.CATEGORY)?:
                getSerializable(Category.CATEGORY)) as Category

        pointOfContact = (savedInstanceState?.getSerializable(
                PointOfContactData.PointOfContact.POINT_OF_CONTACT)?:
                getParcelable<PointOfContactData.PointOfContact>(
                        PointOfContactData.PointOfContact.POINT_OF_CONTACT))
                as PointOfContactData.PointOfContact
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(Category.CATEGORY, category)
        outState?.putParcelable(PointOfContactData.PointOfContact.POINT_OF_CONTACT, pointOfContact)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        presenter.cancel()
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        build()
        presenter.loadProducts(pointOfContact?.id!!,"", category?.id!!.toInt())
    }

    private fun build(){
        txtProductListTitle.text = activity.getString(R.string.product_list_welcome_msg) + ZxVenturesApp.name

        productsRecycler.layoutManager = GridLayoutManager(activity, COLUMNS)

        adapter = ProductListAdapter(activity, products, {
            getBaseActivity<ProductActivity>().startActivity(
                    ProductDetailActivity::class.java, it)
        })

        productsRecycler.adapter = adapter

        productsRecycler.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                productsRecycler.viewTreeObserver.removeOnPreDrawListener(this)
                for (i in 0..(productsRecycler.childCount - 1)) {
                    val v = productsRecycler.getChildAt(i)
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

        productsRecycler.addItemDecoration(object: RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View,
                                        parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = SPACE_BETWEEN_ITEMS
                outRect.right = SPACE_BETWEEN_ITEMS
                outRect.bottom = SPACE_BETWEEN_ITEMS
                outRect.top = SPACE_BETWEEN_ITEMS
            }
        })
    }

    override fun showProducts(products: MutableList<Product>) {
        progress?.visibility = View.GONE
        this.products.addAll(products)
        adapter?.notifyDataSetChanged()
    }

    override fun showError(e: Exception) {
        if(isNetworkConnected()){
            val msg = when(e){
                is ApolloNetworkException -> getString(R.string.problem_connection)
                is ApolloHttpException -> getString(R.string.problem_connection)
                else -> getString(R.string.generic_error)
            }
            getBaseActivity<ProductActivity>().showSnack(this.view, msg)
            showEmptyState(getString(R.string.product_list_empty_data))
        }else{
            getBaseActivity<ProductActivity>().showSnack(this.view, getString(R.string.no_internet_connection))
            showEmptyState(getString(R.string.no_internet_connection))
        }
    }

    override fun showEmptyState(msg: String) {
        progress?.visibility = View.GONE
        txtEmptyState?.visibility = View.VISIBLE

        txtEmptyState?.text = msg
    }
}


