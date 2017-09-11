package com.zxventures.zxapp.base

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId

class Events {

    fun eventOnView(@IdRes viewId: Int, vararg actions: ViewAction): ViewInteraction? {
        return onView(withId(viewId)).perform(*actions)
    }
}
