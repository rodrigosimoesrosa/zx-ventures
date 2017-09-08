package com.zxventures.zxapp.base

import android.app.Activity
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.zxventures.zxapp.Matchers
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Created by rodrigosimoesrosa
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class AcceptanceActivityTest<T : Activity>(clazz: Class<T>) {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<T> = ActivityTestRule(clazz)

    @Rule
    @JvmField
    val intentRule: ActivityTestRule<T> = ActivityTestRule(clazz)

    val check: Matchers = Matchers()
    val event: Events = Events()
}
