package com.hubtele.android.helper

import com.hubtele.android.BuildConfig
import com.hubtele.android.Constants
import com.hubtele.android.ui.activity.TopActivity
import com.hubtele.android.ui.helper.ReviewDialogHelper
import com.hubtele.android.util.PrefUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import java.util.*

/**
 * Created by nakama on 2016/02/04.
 */
//@RunWith(RobolectricGradleTestRunner::class)
val ELEVEN_MINUTE_MSEC = 11 * 60 * 1000
val NINE_MINUTE_MSEC = 9 * 60 * 1000

@RunWith(Enclosed::class)
class ReviewDialogHelperTest {


    @RunWith(RobolectricGradleTestRunner::class)
    @Config(constants = BuildConfig::class, sdk = intArrayOf(21))
    class count_see_achieveが0回の状態 {
        lateinit var sut: ReviewDialogHelper
        @Before fun before() {
            PrefUtil.saveInt(Constants.PrefKey.COUNT_SEE_ACHIEVE, 0)
            sut = spy(ReviewDialogHelper())
        }

        @Test fun 一回見ても表示しない() {
            val activity = Robolectric.buildActivity(TopActivity::class.java).create().get()
            sut.showDialogIfTiming(activity, Date().time - ELEVEN_MINUTE_MSEC)

            verify(sut,never()).showDialog(activity)
        }

        @Test fun 二回見たら表示する() {
            val activity = Robolectric.buildActivity(TopActivity::class.java).create().get()
            sut.showDialogIfTiming(activity, Date().time - ELEVEN_MINUTE_MSEC)
            sut.showDialogIfTiming(activity, Date().time - ELEVEN_MINUTE_MSEC)

            verify(sut).showDialog(activity)
        }
    }

    @RunWith(RobolectricGradleTestRunner::class)
    @Config(constants = BuildConfig::class, sdk = intArrayOf(21))
    class count_see_achieveが1回の状態 {
        lateinit var sut: ReviewDialogHelper
        @Before fun before() {
            PrefUtil.saveInt(Constants.PrefKey.COUNT_SEE_ACHIEVE, 1)
            sut = spy(ReviewDialogHelper())
        }

        @Test fun もう1回10分みたらshowDialog() {
            //            ReviewDialogHelper.showDialogIfTiming()
            val activity = Robolectric.buildActivity(TopActivity::class.java).create().get()
            sut.showDialogIfTiming(activity, Date().time - ELEVEN_MINUTE_MSEC)

            verify(sut).showDialog(activity)
        }

        @Test fun 見た時間が短ければ表示しない() {
            val activity = Robolectric.buildActivity(TopActivity::class.java).create().get()
            sut.showDialogIfTiming(activity, Date().time - NINE_MINUTE_MSEC)
            verify(sut, never()).showDialog(activity)

        }

        @Test fun test2() {
            println("test2ee")
            Assert.assertTrue(true)
        }
    }

    @RunWith(RobolectricGradleTestRunner::class)
    @Config(constants = BuildConfig::class, sdk = intArrayOf(21))
    class neverの状態 {
        lateinit var sut: ReviewDialogHelper
        @Before fun before() {
            PrefUtil.saveInt(Constants.PrefKey.COUNT_SEE_ACHIEVE, 1)
            PrefUtil.saveBoolean(Constants.PrefKey.SHALL_NEVER_REVIEW, true)
            sut = spy(ReviewDialogHelper())
        }

        @Test fun もう1回10分見てもshowDialogされない() {
            //            ReviewDialogHelper.showDialogIfTiming()
            val activity = Robolectric.buildActivity(TopActivity::class.java).create().get()
            sut.showDialogIfTiming(activity, Date().time - ELEVEN_MINUTE_MSEC)

            verify(sut, never()).showDialog(activity)
        }

    }
}