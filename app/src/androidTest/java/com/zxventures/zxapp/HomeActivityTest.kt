package com.zxventures.zxapp

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import com.google.android.gms.location.places.AutocompletePrediction
import com.zxventures.zxapp.base.AcceptanceActivityTest
import com.zxventures.zxapp.screen.home.HomeActivity
import com.zxventures.zxapp.screen.home.adapter.PointOfContactItem
import org.hamcrest.Matchers.*
import org.junit.Test

/**
 * Created by rodrigosimoesrosa
 */
class HomeActivityTest : AcceptanceActivityTest<HomeActivity>(HomeActivity::class.java){

    override fun buildRule(clazz: Class<HomeActivity>): ActivityTestRule<HomeActivity> = ActivityTestRule(clazz)

    private val SEARCH_CORRECT = "Rua Americo"
    private val SEARCH_WRONG = "Moscou"

    @Test
    fun inputCorrectAddress(){
        event.eventOnView(R.id.inputAddress, click(), typeText(SEARCH_CORRECT))

        onData(instanceOf(AutocompletePrediction::class.java))
                .atPosition(0)
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .check(matches(isDisplayed()))
                .perform(click())
        event.clickItem<PointOfContactItem>(R.id.pointOfContactRecycler,0)
    }

    @Test
    fun inputCorrectAddressAndBack(){
        event.eventOnView(R.id.inputAddress, click(), typeText(SEARCH_CORRECT))
        onData(instanceOf(AutocompletePrediction::class.java))
                .atPosition(0)
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .check(matches(isDisplayed()))
                .perform(click())
        event.eventOnView(R.id.pointOfContactRecycler,
                actionOnItemAtPosition<PointOfContactItem>(0, click()))
        Espresso.pressBack()
        event.clickItem<PointOfContactItem>(R.id.pointOfContactRecycler,0)
    }

    @Test
    fun inputAddressAndCancel(){
        event.eventOnView(R.id.inputAddress, click(), typeText(SEARCH_CORRECT))
        event.eventOnView(R.id.btnClose, click())
    }

    @Test
    fun inputAddressAndCancelAfterLoaded(){
        event.eventOnView(R.id.inputAddress, click(), typeText(SEARCH_CORRECT))
        onData(instanceOf(AutocompletePrediction::class.java))
                .atPosition(0)
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .check(matches(isDisplayed()))
                .perform(click())

        event.eventOnView(R.id.btnClose, click())
    }

    @Test
    fun inputWrongAddress(){
        event.eventOnView(R.id.inputAddress, click(),typeText(SEARCH_WRONG))
        onData(instanceOf(AutocompletePrediction::class.java))
                .atPosition(0)
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .check(matches(isDisplayed()))
                .perform(click())
        check.viewDisplayed(R.id.txtAlert)
    }
}
