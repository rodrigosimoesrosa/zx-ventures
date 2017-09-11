package com.zxventures.zxapp.base.matchers

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher


/**
 * Created by rodrigosimoesrosa
 */
class RecyclerViewMatcher {

    companion object {
        fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {

            checkNotNull(itemMatcher)

            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has item at position $position: ")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView): Boolean {
                    val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                    return itemMatcher.matches(viewHolder.itemView)
                }
            }
        }

        fun haveItems(amount: Int, itemMatcher: Matcher<View>): Matcher<View> {

            checkNotNull(itemMatcher)

            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has item ")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView): Boolean {
                    return view.adapter.itemCount == amount
                }
            }
        }
    }
}



