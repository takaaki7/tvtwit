package com.hubtele.android.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.analytics.HitBuilders
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.componentbuilder.SceneComponentBuilder
import com.hubtele.android.data.repository.ProgramRepository
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.Program
import com.hubtele.android.ui.helper.ActivityHelper
import com.hubtele.android.ui.helper.InputHelper
import java.util.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_search.*;
import kotlinx.android.synthetic.main.fragment_search.view.*;
import kotlinx.android.synthetic.main.row_search_result.view.*
import org.parceler.Parcels
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment() {
    override val contentViewId: Int = R.layout.fragment_search

    private var searchedTitle: String? = null
    @Inject lateinit var programRepository: ProgramRepository

    private var adapter: SearchResultAdapter?=null


    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SearchResultAdapter(ArrayList<Program>(), object : SearchResultAdapter.OnRowClickListener {
            override fun onRowClicked(position: Int) {
                ActivityHelper.goBoardActivity(activity, adapter!!.getItem(position))
            }
        }, { search: SearchView, progress: View ->
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.length > 50) {
                        showSnackBar(searchRecyclerView, getString(R.string.search_edit_count_error));
                        search.setQuery(newText.substring(0, 50), false)
                    }
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query == null) {
                        showSnackBar(searchRecyclerView, getString(R.string.search_title_no_word_error));
                    } else if (query.length > 50) {
                        showSnackBar(searchRecyclerView, getString(R.string.search_edit_count_error));
                    } else {
                        searchedTitle = query
                        sendGASearch(searchedTitle!!)
                        progress.visibility = View.VISIBLE
                        //                    adapter.notifyItemChanged(0)
                        bind(programRepository.search(query)
                                .delay(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    progress.visibility = View.GONE
                                    if (it.size == 0) {
                                        showSnackBar(searchRecyclerView, getString(R.string.search_no_programs_found))
                                    }
                                    adapter?.clear()
                                    adapter?.addAll(it)
                                    adapter?.notifyDataSetChanged()
                                }, { e ->
                                    progress.visibility = View.GONE
                                    Timber.e(e.message);
                                }))
                    }
                    InputHelper.hideKeyboard(search)
                    return true
                }

            })
        })
        SceneComponentBuilder.init((activity.application as MyApplication).component).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_search, container, false)
        v.searchRecyclerView.adapter = adapter
        v.searchRecyclerView.layoutManager = LinearLayoutManager(activity)
        Timber.d("onCreateView saved:" + savedInstanceState);
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            var list = Parcels.unwrap<ArrayList<Program>?>(savedInstanceState.getParcelable("results"))
            if (list != null) {
                adapter?.dataList = list
                adapter?.savedSearchTitle = savedInstanceState.getString("searchedTitle")
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchedTitle", searchedTitle)
        outState.putParcelable("results", Parcels.wrap(adapter?.dataList))
    }

    private fun sendGASearch(title:String){
        tracker.send(HitBuilders.EventBuilder()
                .setCategory("other")
                .setAction(title)
                .setLabel(title)
                .build())
    }

    class SearchResultAdapter(var dataList: ArrayList<Program>
                              , val listener: SearchResultAdapter.OnRowClickListener
                              , val onSearchSet: ((v: SearchView, progress: View) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int = dataList.size + 1//plus 1 for header

        var savedSearchTitle: String? = null
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            Timber.d("hohohohi")

            if (position == 0 && holder?.itemViewType == TYPE_HEADER) {
                val search = ((holder?.itemView as ViewGroup).findViewById(R.id.search_view) as SearchView)
                if (savedSearchTitle != null) {
                    search.setQuery(savedSearchTitle, false)
                    savedSearchTitle = null
                }
                onSearchSet(search, ((holder?.itemView as ViewGroup).findViewById(R.id.progress_wheel)))
            } else {
                (holder as SearchResultViewHolder).initProgram(dataList[position - 1])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
            if (viewType == TYPE_HEADER) {
                return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_bar, parent, false)) {
                }
            } else {
                return SearchResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_search_result, parent, false), listener)
            }
        }


        interface OnRowClickListener {
            fun onRowClicked(position: Int)
        }


        class SearchResultViewHolder(val parent: View, listener: OnRowClickListener) : RecyclerView.ViewHolder(parent) {
            var program: Program? = null

            init {
                parent.setOnClickListener { v -> listener.onRowClicked(adapterPosition - 1) }
            }

            fun initProgram(program: Program) {
                this.program = program
                parent.titleText.text = program.title
                parent.dateText.text = DateFormatter.toDateHmFormat(program.startAt)
                parent.entryCountText.text = program.entryCount.toString()
            }
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) {
                return TYPE_HEADER
            } else {
                return super.getItemViewType(position)
            }
        }

        fun addAll(list: List<Program>) = dataList.addAll(list)

        fun clear() = dataList.clear()

        override fun getItemId(position: Int): Long = 0

        fun getItem(position: Int) = dataList[position]

        companion object {
            val TYPE_HEADER: Int = Integer.MIN_VALUE
        }
    }
}