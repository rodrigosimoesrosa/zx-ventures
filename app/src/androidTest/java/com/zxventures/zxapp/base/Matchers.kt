package com.zxventures.zxapp

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import com.zxventures.zxapp.base.matchers.RecyclerViewMatcher.Companion.atPosition
import com.zxventures.zxapp.base.matchers.RecyclerViewMatcher.Companion.haveItems
import org.hamcrest.Matchers

class Matchers {

    fun viewExist(@IdRes viewId: Int){
        onView(withId(viewId)).check(doesNotExist())
    }

    fun viewDisplayed(@IdRes viewId: Int){
        onView(withId(viewId)).check(matches(isDisplayed()))
    }

    fun <T> recyclerViewPosition(@IdRes viewId: Int, position:Int, viewHolderClass: Class<T>){
        onView(withId(viewId)).check(matches(atPosition(position, Matchers.instanceOf(viewHolderClass))))
    }

    fun <T> recyclerViewHaveItems(@IdRes viewId: Int, amount: Int, viewHolderClass: Class<T>){
        onView(withId(viewId)).check(matches(haveItems(amount, Matchers.instanceOf(viewHolderClass))))
    }

    fun <T:Activity> nextActivityWhichWillOpenIs(clazz: Class<T>) {
        intended(IntentMatchers.hasComponent(clazz.name))
    }

    fun viewIsVisibleAndContainsText(@StringRes stringResource: Int) {
        onView(withText(stringResource)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun viewContainsText(@IdRes viewId: Int, @StringRes stringResource: Int) {
        onView(withId(viewId)).check(matches(withText(stringResource)))
    }
}


