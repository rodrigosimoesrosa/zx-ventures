package com.zxventures.zxapp.screen.product

import CategorysQuery
import PocCategorySearchQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.api.ZxVentureAPI
import com.zxventures.zxapp.base.mvp.BaseMVPPresenter
import com.zxventures.zxapp.base.mvp.BaseMVPPresenterImpl
import com.zxventures.zxapp.base.mvp.BaseMVPView
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.model.Product

/**
 * Created by rodrigosimoesrosa
 */
object ProductContract {

    val instance = ZxVenturesApp.instance

    private val EMPTY_DATA_PRODUCT = instance.getString(R.string.product_list_empty_data)
    private val EMPTY_DATA_CATEGORY = instance.getString(R.string.category_empty_data)

    interface ProductCategoryView : BaseMVPView {
        fun showCategorys(categorys: MutableList<Category>)
        fun showError(e: Exception)
        fun showEmptyState(msg: String)
    }

    interface ProductCategoryPresenter : BaseMVPPresenter<ProductCategoryView> {
        fun loadCategorys()
        fun cancel()
    }

    class ProductCategoryPresenterImpl : BaseMVPPresenterImpl<ProductCategoryView>(),
            ProductCategoryPresenter {

        private var client: ApolloQueryCall<CategorysQuery.Data>? = null

        override fun cancel() {
            client?.cancel()
        }

        override fun loadCategorys() {
            val apolloClient: ApolloClient = ApolloClient.builder()
                    .serverUrl(ZxVentureAPI.API)
                    .build()

            client = apolloClient.query(CategorysQuery.builder().build())
            client?.enqueue(object : ApolloCall.Callback<CategorysQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    view?.let { view -> call(view, e, view::showError) }
                }

                override fun onResponse(response: Response<CategorysQuery.Data>) {
                    val list:MutableList<Category> = mutableListOf()
                    response.data()?.allCategory()?.map { list.add(Category.build(it)) }
                    if(list.isEmpty()) {
                        view?.let { view -> call(view, EMPTY_DATA_CATEGORY, view::showEmptyState) }
                    }else {
                        /**
                         * Default id for category "all" or without filter
                         */
                        list.add(0, Category(instance.getString(R.string.category_all_id),
                                instance.getString(R.string.category_all)))
                        view?.let { view -> call(view, list, view::showCategorys) }
                    }
                }
            })
        }
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

    class ProductListPresenterImpl : BaseMVPPresenterImpl<ProductListView>(),
            ProductListPresenter {

        private var client: ApolloQueryCall<PocCategorySearchQuery.Data>? = null

        override fun cancel() {
            client?.cancel()
        }

        override fun loadProducts(id:String, search: String, categoryId:Int) {
            val apolloClient: ApolloClient = ApolloClient.builder()
                    .serverUrl(ZxVentureAPI.API)
                    .build()

            client = apolloClient.query(PocCategorySearchQuery.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .search(search)
                    .build())

            client?.enqueue(object : ApolloCall.Callback<PocCategorySearchQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    view?.let { view -> call(view, e, view::showError) }
                }

                override fun onResponse(response: Response<PocCategorySearchQuery.Data>) {
                    val list:MutableList<Product> = mutableListOf()
                    response.data()?.poc()?.products()?.map {
                        it.productVariants()?.map {
                            list.add(Product.build(it))
                        }
                    }
                    if(list.isEmpty()) {
                        view?.let { view -> call(view, EMPTY_DATA_PRODUCT, view::showEmptyState) }
                    }else{
                        view?.let { view -> call(view, list, view::showProducts) }
                    }
                }
            })
        }
    }
}