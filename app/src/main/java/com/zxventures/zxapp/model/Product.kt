package com.zxventures.zxapp.model

import PocCategorySearchQuery
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
data class Product(val title: String, val description: String?, val price: Double?, val imageUrl: String?) : Serializable {

    companion object {
        val PRODUCT = "PRODUCT"

        fun build(obj: PocCategorySearchQuery.ProductVariant) : Product {
            return Product(obj.title(), obj.description(), obj.price(), obj.imageUrl())
        }
    }
}