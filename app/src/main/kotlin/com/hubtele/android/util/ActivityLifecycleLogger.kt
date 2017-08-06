package com.hubtele.android.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber

class ActivityLifecycleLogger :Application.ActivityLifecycleCallbacks{
    override fun onActivityPaused(activity: Activity?) {
        Timber.d("onActivityPaused:"+(activity?.javaClass?.getName()))
    }

    override fun onActivityStarted(activity: Activity?) {
        Timber.d("onActivityStarted:"+(activity?.javaClass?.getName()))
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Timber.d("onActivityDestroyed:"+(activity?.javaClass?.getName()))
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Timber.d("onActivitySaveInstanceState:"+(activity?.javaClass?.getName()))
    }

    override fun onActivityStopped(activity: Activity?) {
        Timber.d("onActivityStopped:"+(activity?.javaClass?.getName()))
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Timber.d("onActivityCreated:"+(activity?.javaClass?.getName()))
    }

    override fun onActivityResumed(activity: Activity?) {
        Timber.d("onActivityResumed:"+(activity?.javaClass?.getName()))
    }
}