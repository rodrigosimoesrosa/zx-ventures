package com.zxventures.zxapp.presenter.product.impl

import PocCategorySearchQuery
import br.com.mirabilis.util.io.GsonUtil
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.base.mvp.BaseMVPPresenterImpl
import com.zxventures.zxapp.model.Product
import com.zxventures.zxapp.presenter.product.contract.ProductContract
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
class ProductListPresenterImpl : BaseMVPPresenterImpl<ProductContract.ProductListView>(),
        ProductContract.ProductListPresenter {

    val instance = ZxVenturesApp.instance

    override fun loadProducts(id: String, search: String, categoryId: Int) {
        val list: MutableList<Product> = mutableListOf()

        GsonUtil.toSerialize(
                instance.resources.openRawResource(R.raw.products),
                ProductMockup::class.java).poc.products()?.map {
            it.productVariants()?.map {
                list.add(Product.build(it))
            }
        }

        view?.let { view -> call(view, list, view::showProducts)}
    }

    override fun cancel() {}

    data class ProductMockup(val poc: PocCategorySearchQuery.Poc) : Serializable
}