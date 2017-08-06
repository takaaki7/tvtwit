package com.hubtele.android.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.WindowManager
import com.google.android.gms.analytics.HitBuilders
import com.hubtele.android.Constants
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.Program
import com.hubtele.android.ui.view.ChatToolBar
import com.hubtele.android.ui.view.Scroller
import kotlinx.android.synthetic.main.activity_timeshift.*
import org.parceler.Parcels
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseChatActivity : BaseActivity() {
    abstract override val contentViewId: Int
    private val GA_INTERVAL_MSEC = 30000L
    private var proceededTime = 0L
    lateinit var program: Program

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultIntent=Intent()
        resultIntent.putExtra(Constants.IntentExtraKey.LONG_CHAT_CREATED,Date().time)
        setResult(Activity.RESULT_OK,resultIntent);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        program = Parcels.unwrap(intent.getParcelableExtra(Constants.IntentExtraKey.PROGRAM))
        bind(Observable.interval(GA_INTERVAL_MSEC, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    proceededTime += GA_INTERVAL_MSEC/1000
                    tracker.send(HitBuilders.EventBuilder()
                    .setCategory("chat")
                    .setAction("see")
                    .setValue(proceededTime)
                    .build())
                }, { e -> Timber.e(e.message); }))
    }

}