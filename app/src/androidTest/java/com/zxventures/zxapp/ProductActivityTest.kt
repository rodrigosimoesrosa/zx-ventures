package com.zxventures.zxapp

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.action.ViewActions.swipeRight
import android.support.test.rule.ActivityTestRule
import com.zxventures.zxapp.base.AcceptanceActivityTest
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.screen.product.ProductActivity
import com.zxventures.zxapp.screen.product.list.adapter.ProductItem
import org.junit.Test

/**
 * Created by rodrigosimoesrosa
 */
class ProductActivityTest : AcceptanceActivityTest<ProductActivity>(ProductActivity::class.java){

    override fun buildRule(clazz: Class<ProductActivity>): ActivityTestRule<ProductActivity> {
        return object : ActivityTestRule<ProductActivity>(clazz) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                val intent = Intent(targetContext, clazz)
                val point = PointOfContactData.PointOfContact(
                        id = "41" /*valid ID */,
                        officialName = "Teste",
                        tradingName = "Teste",
                        status = PointOfContactData.StatusPoint.AVAILABLE,
                        address = PointOfContactData.Address(
                                address1 = "Rua teste",
                                address2 = "Rua teste",
                                zip = "",
                                city = "",
                                number = "",
                                province = ""))

                intent.putExtra(PointOfContactData.PointOfContact.POINT_OF_CONTACT, point)
                return intent
            }
        }
    }

    @Test
    fun clickItem(){
        event.clickItem<ProductItem>(R.id.productsRecycler,0)
    }

    @Test
    fun scrollItem(){
        event.scrollTo<ProductItem>(R.id.productsRecycler,4)
        event.clickItem<ProductItem>(R.id.productsRecycler,4)
        Espresso.pressBack()
    }

    @Test
    fun swipeCategory(){
        event.eventOnView(R.id.viewPager, swipeLeft())
        event.eventOnView(R.id.viewPager, swipeRight())
        event.eventOnView(R.id.viewPager, swipeLeft())
        event.eventOnView(R.id.viewPager, swipeLeft())
        event.eventOnView(R.id.viewPager, swipeLeft())
    }
}