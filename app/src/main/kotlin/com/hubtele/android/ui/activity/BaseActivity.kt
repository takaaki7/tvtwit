package com.hubtele.android.ui.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.hubtele.android.MyApplication
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

open abstract class BaseActivity : AppCompatActivity() {
    abstract val contentViewId: Int;
    val subscriptions = CompositeSubscription();
    protected lateinit var tracker: Tracker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        (application as MyApplication).component.inject(this)
        tracker = (application as MyApplication).getDefaultTracker()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setContentView(contentViewId)
    }

    fun setupToolbar(toolbar: Toolbar) {
        if (Build.VERSION.SDK_INT >= 21) toolbar.elevation = 4f
    }

    abstract fun getPageName(): String?
    override fun onResume() {
        super.onResume()
        if(getPageName() !=null) {
            sendGA(getPageName()!!)
        }
    }

    fun sendGA(pageName:String){
        tracker.setScreenName(pageName)
        tracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    fun view(id: Int): View {
        val view: View? = findViewById(id)
        if (view == null)
            throw IllegalArgumentException("Given ID could not be found in current layout!:" + id)
        return view
    }

    fun showSnackBar(v: View, message: String, length: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(v, message, length).show()
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    fun bind(subscription: Subscription) {
        subscriptions.add(subscription);
    }
}