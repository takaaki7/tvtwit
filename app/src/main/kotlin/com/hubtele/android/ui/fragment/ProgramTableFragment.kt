package com.hubtele.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.hubtele.android.Constants
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.componentbuilder.SceneComponentBuilder
import com.hubtele.android.model.Program
import com.hubtele.android.model.ProgramInfo
import com.hubtele.android.model.ProgramTable
import com.hubtele.android.data.repository.ProgramRepository
import com.hubtele.android.data.repository.errorLogHandler
import com.hubtele.android.ui.activity.ChatActivity
import com.hubtele.android.ui.adapter.ProgramTableAdapter
import com.hubtele.android.ui.helper.ActivityHelper
import com.hubtele.android.ui.helper.ProgramTableSpaceDecorator
import com.hubtele.android.util.ScreenUtil
import org.parceler.Parcels
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber

import javax.inject.Inject
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class ProgramTableFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    override val contentViewId: Int
        get() = R.layout.fragment_program_table

    @Inject lateinit var programRepository: ProgramRepository

    private var adapter: ProgramTableAdapter = ProgramTableAdapter(ArrayList<ProgramInfo>(0), object : ProgramTableAdapter.OnCardClickListener {
        override fun onClicked(info:ProgramInfo) {
            ActivityHelper.goBoardActivity(activity,info.program)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SceneComponentBuilder.init((activity.application as MyApplication).component).inject(this)
        bind(programRepository.getProgramTable().subscribe({
            it.values.forEach { adapter.add(it) }
            adapter.notifyDataSetChanged();
        }, { e -> Timber.e(e.message); }))

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_program_table, container, false)
        recyclerView = v.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = GridLayoutManager(activity, 2)
        recyclerView!!.addItemDecoration(
                ProgramTableSpaceDecorator(ScreenUtil.dpToPx(8), ScreenUtil.dpToPx(4)))

        setupSwipeRefresh(v)
        return v
    }

    private fun setupSwipeRefresh(v: View) {
        var swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout?
        swipeContainer?.setOnRefreshListener {
            bind(programRepository.getProgramTable()
                    .delay(1500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        adapter.clear()
                        it.values.forEach { adapter.add(it) }
                        adapter.notifyDataSetChanged();
                        swipeContainer.isRefreshing = false;
                    }, { e ->
                        Timber.e(e.message);
                        swipeContainer.isRefreshing = false;
                    }))
        }

        swipeContainer?.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    companion object {
        fun newInstance(): ProgramTableFragment = ProgramTableFragment()
    }
}
