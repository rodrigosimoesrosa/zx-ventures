package com.zxventures.zxapp.base

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.allOf

class Events {

    fun <T : RecyclerView.ViewHolder> scrollTo(@IdRes viewId: Int, position: Int): ViewInteraction? {
        return onView(allOf(isDisplayed(), withId(viewId))).perform(scrollToPosition<T>(position))
    }

    fun <T : RecyclerView.ViewHolder> clickItem(@IdRes viewId: Int, position: Int){
        onView(allOf(isDisplayed(), withId(viewId)))
                .perform(actionOnItemAtPosition<T>(position, click()))
    }

    fun eventOnView(@IdRes viewId: Int, vararg actions: ViewAction): ViewInteraction? {
        return onView(allOf(isDisplayed(), withId (viewId))).perform(*actions)
    }
}
