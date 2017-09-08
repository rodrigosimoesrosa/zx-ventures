package com.zxventures.zxapp.screen.product.list.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zxventures.zxapp.model.Category
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.screen.product.list.ProductListFragment

/**
 * Created by rodrigosimoesrosa
 */
class ProductAdapter(private val categorys: MutableList<Category>,
                     private val pointOfContact: PointOfContactData.PointOfContact,
                     fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int =  categorys.size

    override fun getItem(i: Int): ProductListFragment {
        return ProductListFragment.build(categorys[i], pointOfContact)
    }

    override fun getPageTitle(position: Int): String {
        return categorys[position].title ?: ""
    }
}