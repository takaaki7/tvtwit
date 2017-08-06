package com.hubtele.android.util

import android.content.res.Resources
import android.support.test.espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by nakama on 2015/11/11.
 */
fun firstChildOf(parentMatcher: Matcher<View>): Matcher<View> {
    return childAt(parentMatcher, 0)
}

fun secondChildOf(parentMatcher: Matcher<View>): Matcher<View> {
    return childAt(parentMatcher, 1)
}

fun thirdChildOf(parentMatcher: Matcher<View>): Matcher<View> {
    return childAt(parentMatcher, 2)
}

fun childAt(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with ${position + 1} child view of type parentMatcher")
        }

        override fun matchesSafely(view: View): Boolean {

            if (view.getParent() !is ViewGroup) {
                return parentMatcher.matches(view.getParent())
            }
            val group = view.getParent() as ViewGroup
            return parentMatcher.matches(view.getParent()) && group.getChildAt(position)?.equals(view) ?:false

        }
    }
}

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {

    return RecyclerViewMatcher(recyclerViewId)
}

/**
 * Created by dannyroa on 5/10/15.
 */
class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            internal var resources: Resources? = null
            internal var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = "%s (resource name not found)".format(arrayOf<Int>(Integer.valueOf(recyclerViewId)))
                    }

                }

                description.appendText("with id: " + idDescription + ",position:" + position)
            }

            override fun matchesSafely(view: View): Boolean {

                this.resources = view.getResources()

                if (childView == null) {
                    val recyclerView = view.getRootView().findViewById(recyclerViewId) as RecyclerView
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.getChildAt(position)
                    } else {
                        return false
                    }
                }

                if (targetViewId == -1) {
                    return view === childView
                } else {
                    val targetView = childView!!.findViewById(targetViewId)
                    return view === targetView
                }

            }
        }
    }
}


/** Perform action of waiting for a specific view id.  */
fun waitId(viewId: Int, millis: Long): ViewAction {
    return object : ViewAction {

        override fun getConstraints(): Matcher<View>? {
            return isRoot()
        }

        override fun getDescription(): String? {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher = withId(viewId)

            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }

                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)

            // timeout happens
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
        }
    }
}


class ElapsedTimeIdlingResource(private val waitingTime: Long) : IdlingResource {
    private val startTime: Long
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    init {
        this.startTime = System.currentTimeMillis()
        setEspressoPolicyTimeout(waitingTime * 2)
    }

    override fun getName(): String? = ElapsedTimeIdlingResource::class.java!!.getName() + ":" + waitingTime

    override fun isIdleNow(): Boolean {
        val elapsed = System.currentTimeMillis() - startTime
        val idle = (elapsed >= waitingTime)
        if (idle) {
            resourceCallback!!.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(
            resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    fun waitStart() {
        val waitingTime = DateUtils.HOUR_IN_MILLIS

        // Now we wait
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        Espresso.registerIdlingResources(idlingResource)

        // Clean up
        Espresso.unregisterIdlingResources(idlingResource)
    }

}

fun setEspressoPolicyTimeout(timeoutMsec: Long) {
    // Make sure Espresso does not time out
    IdlingPolicies.setMasterPolicyTimeout(
            timeoutMsec, TimeUnit.MILLISECONDS)
    IdlingPolicies.setIdlingResourceTimeout(
            timeoutMsec, TimeUnit.MILLISECONDS)

}