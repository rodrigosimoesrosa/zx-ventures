package com.zxventures.zxapp.presenter.product.impl

import CategorysQuery
import br.com.mirabilis.util.io.GsonUtil
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.base.mvp.BaseMVPPresenterImpl
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.presenter.product.contract.ProductContract
import java.io.Serializable
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
class ProductCategoryPresenterImpl : BaseMVPPresenterImpl<ProductContract.ProductCategoryView>(),
        ProductContract.ProductCategoryPresenter {

    val instance = ZxVenturesApp.instance

    override fun loadCategorys() {
        val list:MutableList<Category> = mutableListOf()

        GsonUtil.toSerialize(
                instance.resources.openRawResource(R.raw.category),
                CategoryMockup::class.java).allCategory.map {
            list.add(Category.build(it))
        }

        view?.let { view -> call(view, list, view::showCategorys)}
    }

    override fun cancel() {}

    data class CategoryMockup(val allCategory: ArrayList<CategorysQuery.AllCategory>): Serializable
}