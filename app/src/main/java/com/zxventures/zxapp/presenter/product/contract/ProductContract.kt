package com.zxventures.zxapp.presenter.product.contract

import com.zxventures.zxapp.base.mvp.BaseMVPPresenter
import com.zxventures.zxapp.base.mvp.BaseMVPView
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.model.Product

/**
 * Created by rodrigosimoesrosa
 */
object ProductContract {

    interface ProductCategoryView : BaseMVPView {
        fun showCategorys(categorys: MutableList<Category>)
        fun showError(e: Exception)
        fun showEmptyState(msg: String)
    }

    interface ProductCategoryPresenter : BaseMVPPresenter<ProductCategoryView> {
        fun loadCategorys()
        fun cancel()
    }

    interface ProductListView : BaseMVPView {
        fun showProducts(products: MutableList<Product>)
        fun showError(e: Exception)
        fun showEmptyState(msg: String)
    }

    interface ProductListPresenter : BaseMVPPresenter<ProductListView> {
        fun loadProducts(id:String,search: String, categoryId:Int)
        fun cancel()
    }
}