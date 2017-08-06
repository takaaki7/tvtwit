package com.hubtele.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hubtele.android.Constants
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.componentbuilder.SceneComponentBuilder
import com.hubtele.android.data.repository.RankingRepository
import com.hubtele.android.ui.activity.TimeShiftActivity
import com.hubtele.android.ui.adapter.RankingRecyclerAdapter
import com.hubtele.android.ui.helper.ActivityHelper
import com.hubtele.android.ui.helper.EndlessOnScrollListener
import com.hubtele.android.ui.helper.RankingListDividerDecoration
import kotlinx.android.synthetic.main.fragment_ranking.*;
import kotlinx.android.synthetic.main.fragment_ranking.view.*;
import org.parceler.Parcels
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RankingFragment : BaseFragment() {
    override val contentViewId: Int = R.layout.fragment_ranking;

    private val ROWS_PER_PAGE = 35;

    @Inject lateinit var rankingRepository: RankingRepository;

    private var adapter: RankingRecyclerAdapter? = null;

    private var endlessScrollListener: EndlessOnScrollListener? = null

    companion object {
        fun newInstance(): RankingFragment = RankingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SceneComponentBuilder.init((activity.application as MyApplication).component).inject(this);
        adapter = RankingRecyclerAdapter(arrayListOf(), object : RankingRecyclerAdapter.OnRankRowClickListener {
            override fun onRankRowClick(v: View, position: Int) {
                ActivityHelper.goBoardActivity(activity, adapter!!.getItemAt(position))
            }
        });
        loadAndSet(0)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_ranking, container, false)
        val recView = v.findViewById(R.id.recyclerView) as RecyclerView
        recView.adapter = adapter;
        recView.layoutManager = LinearLayoutManager(activity)
        endlessScrollListener = object : EndlessOnScrollListener(recView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage == 1) return;
                val start = (currentPage - 1) * ROWS_PER_PAGE;
                loadAndSet(start)
            }
        }
        recView.addOnScrollListener(endlessScrollListener)
        setupSwipeRefresh(v)
        return v
    }

    private fun setupSwipeRefresh(v: View) {
        val swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer.setOnRefreshListener {
            bind(rankingRepository.getRanking(0, ROWS_PER_PAGE)
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        endlessScrollListener?.reset()
                        adapter?.clear()
                        adapter?.addAll(it);
                        adapter?.notifyDataSetChanged();
                        swipeContainer.isRefreshing = false;
                    }, { err: Throwable ->
                        swipeContainer.isRefreshing = false;
                        Timber.e(err.message)
                    }))
        }


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private fun loadAndSet(startIndex: Int) {
        bind(rankingRepository.getRanking(startIndex, ROWS_PER_PAGE).subscribe ({
            adapter?.addAll(it);
            adapter?.notifyDataSetChanged();
        }, { err: Throwable -> Timber.e(err.message) }))
    }

}