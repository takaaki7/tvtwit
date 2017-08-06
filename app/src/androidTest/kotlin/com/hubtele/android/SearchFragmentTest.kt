package com.hubtele.android

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import com.hubtele.android.componentbuilder.AppComponentBuilder
import com.hubtele.android.internal.di.module.StubApiServiceModule
import com.hubtele.android.ui.activity.TopActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.assertion.ViewAssertions.matches;
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.withDecorView;
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import android.support.test.espresso.matcher.ViewMatchers.withText;
import com.hubtele.android.model.Program
import com.hubtele.android.ui.activity.ChatActivity
import com.hubtele.android.ui.activity.TimeShiftActivity
import com.hubtele.android.ui.fragment.SearchFragment
import com.hubtele.android.util.secondChildOf

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import com.hubtele.android.util.thirdChildOf
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`;
import org.hamcrest.Matchers.not;
import org.junit.After

/**
 * Created by nakama on 2015/11/14.
 */
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {
    @Rule @JvmField
    val activityRule = ActivityTestRule<TopActivity>(TopActivity::class.java, true, false)

    @Before
    fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as MyApplication
        app.component = AppComponentBuilder.init(StubApiServiceModule())
        activityRule.launchActivity(Intent())
        Intents.init()
        onView(withId(R.id.viewPager)).perform(swipeLeft(), swipeLeft())
    }

    @After
    fun tear() {
        Intents.release()
    }

    @Test
    fun enterLongText() {
        val overText = (0..60).asSequence().fold("") { e, b -> "$e$b" }
        onSearchViewEdit().perform(typeText(overText))
        onView(withText(overText)).check(doesNotExist())
        onView(withText(overText.substring(0, 50))).check(doesNotExist())
        onView(withText("タイトルは50文字以内で入力してください"))
                .inRoot(withDecorView(not(`is`(activityRule.activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    fun enterEmptyText() {
        onSearchViewEdit().perform(typeText(""), ViewActions.pressImeActionButton())
        onView(withText("検索する番組タイトルを入力してください"))
                .inRoot(withDecorView(not(`is`(activityRule.activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    fun resultIsDisplayedClickAndGoChat() {
        onSearchViewEdit().perform(typeText("1234"), pressImeActionButton())
        onResultView()
                .check(matches(hasDescendant(withText("0title"))))
                .perform(RecyclerViewActions.actionOnItemAtPosition<SearchFragment.SearchResultAdapter.SearchResultViewHolder>
                (1, click()))
        intended(allOf(hasComponent(ChatActivity::class.java.name)))
    }

    @Test
    fun resultIsDisplayedClickAndGoTimeShift() {
        val p = Program()
        p.id = "1id"
        onSearchView().perform(click())
        onSearchViewEdit().perform(typeText("1234"), pressImeActionButton())
        onResultView()
                .check(matches(hasDescendant(withText("1title"))))
                .perform(RecyclerViewActions.actionOnItemAtPosition<SearchFragment.SearchResultAdapter.SearchResultViewHolder>
                (2, click()))
        intended(allOf(hasComponent(TimeShiftActivity::class.java.name),
                hasExtra(Constants.IntentExtraKey.PROGRAM, p)))
    }

    //    @Test
    //    fun keyboardIsHiddenOnSwiped(){
    //        onSearchView().perform(ViewActions.click())
    //        onView(withId(R.id.viewPager)).perform(swipeRight())
    //        .check(Asser)
    //    }

    private fun onSearchView() = onView(withId(R.id.search_view))
    private fun onSearchViewEdit() = onView(withId(R.id.search_src_text))//HACK:SearchView is ViewGroup that hold EditText(id:search_src_text) inside.
    private fun onResultView(): ViewInteraction {
        return onView(withId(R.id.searchRecyclerView));

    }
}