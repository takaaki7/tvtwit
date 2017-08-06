package com.hubtele.android

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.PositionAssertions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.PositionAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.text.format.DateUtils
import com.hubtele.android.componentbuilder.AppComponentBuilder
import com.hubtele.android.internal.di.module.StubApiServiceModule
import com.hubtele.android.internal.di.module.StubChatStreamModule
import com.hubtele.android.model.Program
import com.hubtele.android.ui.activity.ChatActivity
import com.hubtele.android.ui.activity.TimeShiftActivity
import com.hubtele.android.ui.adapter.ChatRecyclerAdapter
import com.hubtele.android.util.DateUtil
import com.hubtele.android.util.ElapsedTimeIdlingResource
import com.hubtele.android.util.setEspressoPolicyTimeout
import com.hubtele.android.util.waitId
import org.hamcrest.Matchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.parceler.Parcels
import java.util.*

/**
 * Created by nakama on 2015/11/04.
 */
@RunWith(AndroidJUnit4::class)
class ChatActivityTest {
    @Rule @JvmField
    var activityRule = ActivityTestRule<ChatActivity>(ChatActivity::class.java, true, false)

    @Before
    fun injectComponent() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as MyApplication
        app.component = AppComponentBuilder.init(StubApiServiceModule(), StubChatStreamModule())
        val intent = Intent(instrumentation.targetContext, ChatActivity::class.java)
        intent.putExtra("sample", "sample1")
        intent.putExtra(Constants.IntentExtraKey.PROGRAM, Parcels.wrap(
                Program(id = "id1", station = "00", title = "testProgram", startAt = Date(), endAt = DateUtil.addHour(Date(), 2), entryCount = 0)
        ))
        activityRule.launchActivity(intent)
    }

    @Test
    fun entryLogIsDisplayed() {
        onRecyclerView().check(matches(not(hasDescendant(withText("content1")))))
        val waitingTime = 500L


        //        setEspressoPolicyTimeout(waitingTime*2)
        //        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        //        Espresso.registerIdlingResources(idlingResource)

        onRecyclerView().perform(RecyclerViewActions.scrollToPosition<ChatRecyclerAdapter.ChatItemViewHolder>(40))
                .check(matches(hasDescendant(withText("content40"))))


        //        Espresso.unregisterIdlingResources(idlingResource)
    }

    @Test
    fun replyIsDisplayed() {
        onRecyclerView().check(matches(not(hasDescendant(withText("content1")))))
        //        val waitingTime = 500L


        //        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        //        Espresso.registerIdlingResources(idlingResource)

        onRecyclerView().perform(RecyclerViewActions.scrollToPosition<ChatRecyclerAdapter.ChatItemViewHolder>(40))
                .check(matches(hasDescendant(withText("content40"))))
                .check(matches(hasDescendant(withText("replyToParent"))))
                .check(matches(hasDescendant(withText("replyToParent2"))))
                .check(matches(hasDescendant(withText("userNameR"))))
                .check(matches(hasDescendant(withText("userNameR2"))))

        //        Espresso.unregisterIdlingResources(idlingResource)
    }

    @Test
    fun entryWithReplyMoveToBottom() {
        onRecyclerView()
                .check(matches(hasDescendant(withText("content20"))))
                .check(PositionAssertions.isAbove(withText("content21")))
        val waitingTime = 2000L
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        Espresso.registerIdlingResources(idlingResource)
        onRecyclerView().perform(RecyclerViewActions.scrollToPosition<ChatRecyclerAdapter.ChatItemViewHolder>(42))
                .check(matches(hasDescendant(withText("content20"))))
        onView(withText("content20"))
                .check(matches(hasDescendant(withText("contentRepTo20"))))
                .check(matches(hasDescendant(withText("userRepTo20"))))
                .check(PositionAssertions.isAbove(withText("contentRepTo20")))
        Espresso.unregisterIdlingResources(idlingResource)

    }
    //    @Test
    //    fun testScroll() {
    //        onView(withId(R.id.recyclerView))
    //                .check(matches(not(hasDescendant(withText("content1")))))
    //                .perform(waitId(R.id.contentText, 600)
    //                        , RecyclerViewActions.scrollToPosition<ChatRecyclerAdapter.ChatItemViewHolder>(40))
    //                .check(matches(hasDescendant(withText("content40"))))
    //    }

    private fun onRecyclerView(): ViewInteraction = onView(withId(R.id.recyclerView))
}