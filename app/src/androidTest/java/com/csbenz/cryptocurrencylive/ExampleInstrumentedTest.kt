package com.csbenz.cryptocurrencylive.ui.pairs.view


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestPairsActivity {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(PairsActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.csbenz.cryptocurrencylive", appContext.packageName)
    }

    @Test
    fun testSanity() {
        val checkLogo = onView(
                allOf(withText("CryptoCurrencyLive"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()))
        //verifying the logo of the app
        checkLogo.check(matches(withText("CryptoCurrencyLive")))

        val checkTheFirstPair = onView(
                allOf(withId(R.id.tv_pair_name), withText("btcusd"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_pairs),
                                        0),
                                0),
                        isDisplayed()))
        //verifying that the "btcusd" pair appears as the first one in the list of pairs
        checkTheFirstPair.check(matches(withText("btcusd")))

        val selectTheFirstPair = onView(
                allOf(withId(R.id.rv_pairs),
                        childAtPosition(
                                withId(R.id.pairs_root_layout),
                                0)))
        //assuming that the "btcusd" pair's position 1 is persistent - it selected by it's position
        //this should be changed if the position might be changed
        selectTheFirstPair.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val checkTheFirstSelectedPair = onView(
                allOf(withText("btcusd"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()))
        //checking that the exactly selected pair appears
        checkTheFirstSelectedPair.check(matches(withText("btcusd")))
        //this thread.sleep is not ideal and further investigation required for a better approach
        Thread.sleep(600)

        val checkSummary = onView(
                allOf(withId(R.id.tv_summary),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.details_root_layout),
                                        0),
                                0),
                        isDisplayed()))
        //check that summary appears
        checkSummary.check(matches(isDisplayed()))

        val switchToTradesTab = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabs_details),
                                0),
                        1),
                        isDisplayed()))
        //switching to Trades right away to verify that switching back to Books works properly
        switchToTradesTab.perform(click())
        Thread.sleep(1000)

        val checkTradesDetails = onView(
                allOf(withId(R.id.rv_trades),
                        childAtPosition(
                                withParent(withId(R.id.vp_details)),
                                0),
                        isDisplayed()))
        //checking trades details appearance
        checkTradesDetails.check(matches(isDisplayed()))

        val switchToBooksTab = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabs_details),
                                0),
                        0),
                        isDisplayed()))
        //switching to Book tab
        switchToBooksTab.perform(click())
        Thread.sleep(1200)

        val checkBidsOnBooksTab = onView(
                allOf(withId(R.id.rv_orders_bid),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        0),
                                0),
                        isDisplayed()))
        //checking bids appearance
        checkBidsOnBooksTab.check(matches(isDisplayed()))

        val checkAsksOnBooksTab = onView(
                allOf(withId(R.id.rv_orders_ask),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        0),
                                1),
                        isDisplayed()))
        //checking asks appearance
        checkAsksOnBooksTab.check(matches(isDisplayed()))

        Espresso.pressBack()

        val checkTheNextPair = onView(
                allOf(withId(R.id.tv_pair_name), withText("ltcusd"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_pairs),
                                        1),
                                0),
                        isDisplayed()))
        //checking that user can return to the list of pairs and appearance of the second one
        //again, assuming that their positions are persistent
        checkTheNextPair.check(matches(withText("ltcusd")))

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
