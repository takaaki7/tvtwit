package com.hubtele.android.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.hubtele.android.Constants
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.componentbuilder.SceneComponentBuilder
import com.hubtele.android.data.repository.EntryRepository
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.Entry
import com.hubtele.android.ui.adapter.ChatRecyclerAdapter
import com.hubtele.android.ui.helper.BoardDecoration
import com.hubtele.android.ui.helper.EndlessOnScrollListener
import com.hubtele.android.ui.view.ChatLayoutManager
import com.hubtele.android.ui.view.Scroller
import com.hubtele.android.util.PrefUtil
import kotlinx.android.synthetic.main.activity_timeshift.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimeShiftActivity : BaseChatActivity() {
    override fun getPageName(): String? = "timeshift:${program.id}"

    private val layoutManager: LinearLayoutManager = ChatLayoutManager(this, 2F);

    private val ENTRY_NUM_PER_PAGE = 150
    override val contentViewId: Int = R.layout.activity_timeshift;
    @Inject lateinit var entryRepository: EntryRepository;
    private lateinit var adapter: ChatRecyclerAdapter;
    private var currentPage: Int = 0
    private var currentTime: Long = 0L
    private var playing = true
    private var dragged = false
    private val loadScrollListener = object : EndlessOnScrollListener(layoutManager, false) {
        override fun onLoadMore(currentPage: Int) {
            entryRepository.timeShiftEntry(program.id, calculatePage()).subscribe({
                insertEntries(it)
            }, { e -> Timber.e(e.message); })
        }
    }
    //    private var mNextCheckPointTime: Long = Long.MAX_VALUE
    private var startTimeMSec: Long = 0L
    private var programDuration: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTimeMSec = program.startAt.time
        programDuration = program.endAt.time - program.startAt.time
        programTotalTimeText.text = " / " + DateFormatter.formatDuration(programDuration)
        toolbar.title = program.title
        toolbar.subtitle = DateFormatter.toHmFormat(program.startAt) + "~" + DateFormatter.toHmFormat(program.endAt)
        SceneComponentBuilder.init((application as MyApplication).component).inject(this)

        setupRecyclerView()
        if (!PrefUtil.loadBooleanDefFalse(Constants.PrefKey.TIMESHIFT_LAUNCHED)) {
            showSnackBar(recyclerView, "下のタイマーに合わせて自動でスクロールします。停止ボタンで自動スクロールを止められます。", Snackbar.LENGTH_LONG)
            PrefUtil.saveBoolean(Constants.PrefKey.TIMESHIFT_LAUNCHED, true)
        }
        bind(entryRepository.timeShiftEntry(program.id, calculatePage()).subscribe({
            insertEntries(it)
            if (it.size > 1) {
                currentTime = it[0].date.time - startTimeMSec
                start()
            }
        }, { e -> Timber.e(e.message); }))

        togglePlayButton.setOnClickListener {
            playing = !playing
            togglePlayButton.text = if (playing) getString(R.string.timeshift_button_pause) else getString(R.string.timeshift_button_start)
        }
    }

    private fun insertEntries(it: List<Entry>) {
        val range = Pair(adapter.itemCount, it.size)
        adapter.addAll(it)
        adapter.notifyItemRangeInserted(range.first, range.second)
        loadScrollListener.onLoaded()
    }

    private fun start() {
        bind(Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    if (playing && !dragged) {
                        if (currentTime < programDuration) currentTime += 1000L
                        currentTimeText.text = DateFormatter.formatDuration(currentTime)
                        scrollToCurrentEntry()
                        //                    fetchIfNeeded()
                    }
                }, { e -> Timber.e(e.message); }))
    }

    private fun scrollToCurrentEntry() {
        var currentLastNextPosition = Math.min(layoutManager.findLastCompletelyVisibleItemPosition() + 1, adapter.itemCount - 1)
        if (adapter.getItem(currentLastNextPosition).date.time - startTimeMSec < currentTime) {
            //should scroll!
            var shouldBottomPosition: Int = currentLastNextPosition
            for (i in currentLastNextPosition..adapter.itemCount - 1) {
                if (adapter.getItem(i).date.time - startTimeMSec < currentTime) {
                    shouldBottomPosition = i
                } else {
                    break
                }
            }
            recyclerView.smoothScrollToPosition(shouldBottomPosition)
        }
    }


    private fun setupRecyclerView() {
        adapter = ChatRecyclerAdapter(arrayListOf(), null)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(BoardDecoration(this))
        recyclerView.addOnScrollListener(loadScrollListener)

        var onDragged = { dragged = true }
        var onDragStopped = {
            if (dragged && adapter.itemCount > 0) {
                currentTime = adapter.getItem(layoutManager.findLastCompletelyVisibleItemPosition()).date.time - startTimeMSec
                dragged = false
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> onDragged()
                    RecyclerView.SCROLL_STATE_IDLE -> onDragStopped()
                }
            }
        })
        scroller.userDragListener = object : Scroller.OnUserDragListener {
            override fun onDraggingStarted() = onDragged()

            override fun onDraggingStopped() = onDragStopped()

        }
        scroller.recyclerView = recyclerView
    }

    private fun calculatePage(): Int {
        val page = currentPage * ENTRY_NUM_PER_PAGE
        currentPage++
        return page
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        toolbar.onScreenTouched(ev)
        return super.dispatchTouchEvent(ev)
    }
}
