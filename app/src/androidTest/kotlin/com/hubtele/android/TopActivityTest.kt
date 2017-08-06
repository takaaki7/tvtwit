package com.hubtele.android

//import android.support.test.espresso.
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.hubtele.android.componentbuilder.AppComponentBuilder
import com.hubtele.android.internal.di.module.StubApiServiceModule
import com.hubtele.android.model.Program
import com.hubtele.android.ui.activity.TimeShiftActivity
import com.hubtele.android.ui.activity.TopActivity
import com.hubtele.android.ui.adapter.RankingRecyclerAdapter
import com.hubtele.android.util.secondChildOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by nakama on 2015/10/31.
 */

@RunWith(AndroidJUnit4::class)
class TopActivityTest {
    @Rule @JvmField
    val activityRule = ActivityTestRule<TopActivity>(TopActivity::class.java, true, false)

    @Before
    fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as MyApplication
        app.component = AppComponentBuilder.init(StubApiServiceModule())
        activityRule.launchActivity(Intent())
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test fun clickItemAndGoTimeShift() {
        //        activityRule.launchActivity(Intent())
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        onRankigRecyclerView().perform(RecyclerViewActions.scrollToPosition<RankingRecyclerAdapter.RankItemViewHolder>(33))
                .check(matches(hasDescendant(withText("33title"))))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RankingRecyclerAdapter.RankItemViewHolder>(33, click()))
        intended(allOf(hasComponent(TimeShiftActivity::class.java.name)))
           /*     onView(allOf(withId(R.id.swipeContainer), secondChildOf(withId(R.id.viewPager)))).perform(scrollTo(withText("32title")))
                onData(allOf(Matchers.`is`(instanceOf(Program::class.java)))).atPosition(33)
                        .perform(scrollTo())
                onData(allOf(Matchers.`is`(instanceOf(Program::class.java)))).atPosition(50).check(matches(withText("50title")))
                onView(withText("32title")).perform(scrollTo())
                onView(allOf(withId(R.id.swipeContainer), secondChildOf(withId(R.id.viewPager))))
                        .check(matches(hasDescendant(withText("44title"))));
                onView(allOf(withId(R.id.swipeContainer), secondChildOf(withId(R.id.viewPager))))
                        .check(matches(not(hasDescendant(withText("80title")))));
                assertTrue { true }*/
    }


    @Test fun lastItemDoesNotExistsUntilScrollToBottom() {
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        onRankigRecyclerView().perform(RecyclerViewActions.scrollToPosition<RankingRecyclerAdapter.RankItemViewHolder>(33))
        onRankigRecyclerView().perform(RecyclerViewActions.scrollToPosition<RankingRecyclerAdapter.RankItemViewHolder>(44))
                .check(matches(hasDescendant(withText("44title"))))
        onRankigRecyclerView().perform(RecyclerViewActions.scrollToPosition<RankingRecyclerAdapter.RankItemViewHolder>(98))
                .check(matches(not(hasDescendant(withText("98title")))))
    }

    private fun onRankigRecyclerView(): ViewInteraction {
        return onView(allOf(isDescendantOfA(allOf(withId(R.id.swipeContainer), secondChildOf(withId(R.id.viewPager)))), withId(R.id.recyclerView)));
    }

    private fun onRankigRecyclerView(matcher: Matcher<View>): ViewInteraction {
        return onView(allOf(isDescendantOfA(allOf(withId(R.id.swipeContainer), secondChildOf(withId(R.id.viewPager)))), matcher));
    }
}