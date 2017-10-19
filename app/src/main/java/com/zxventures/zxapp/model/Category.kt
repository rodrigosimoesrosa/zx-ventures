package com.zxventures.zxapp.model

import CategorysQuery
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
data class Category(val id: String?,val title: String?) : Serializable {

    companion object {
        fun build(obj: CategorysQuery.AllCategory) : Category {
            return Category(obj.id(), obj.title())
        }

        val CATEGORY = "CATEGORY"
    }
}
