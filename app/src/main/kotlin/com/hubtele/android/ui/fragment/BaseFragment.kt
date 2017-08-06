package com.hubtele.android.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.analytics.Tracker
import com.hubtele.android.MyApplication
import rx.Subscription
import rx.subscriptions.CompositeSubscription

open abstract class BaseFragment : Fragment() {
    abstract val contentViewId: Int;
    val subscriptions = CompositeSubscription();
    protected lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracker = (activity.application as MyApplication).getDefaultTracker()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.unsubscribe();
    }

    fun showSnackBar(v: View, message: String) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

    fun bind(subscription: Subscription) = subscriptions.add(subscription);
}