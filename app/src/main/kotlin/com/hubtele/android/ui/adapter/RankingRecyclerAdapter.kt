package com.hubtele.android.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.model.Program
import com.hubtele.android.helper.DateFormatter
import timber.log.Timber
import java.util.*

class RankingRecyclerAdapter(private val itemList: ArrayList<Program>, private val listener: RankingRecyclerAdapter.OnRankRowClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun getItemAt(position: Int) = itemList[position]

    fun add(program: Program) = itemList.add(program)

    fun addAll(programs: List<Program>) = itemList.addAll(programs)

    fun clear() = itemList.clear();

    override fun getItemCount(): Int = itemList.size

    interface OnRankRowClickListener {
        fun onRankRowClick(v: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RankItemViewHolder.newInstance(
                LayoutInflater.from(parent.context).inflate(R.layout.rank_row, parent, false), listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RankItemViewHolder).setProgram(position + 1, itemList[position])
    }


    class RankItemViewHolder(val parent: View, val mListener: OnRankRowClickListener) : RecyclerView.ViewHolder(parent) {
        lateinit var rankTitleTextView: TextView;
        lateinit var rankTextView: TextView;
        lateinit var dateTextView: TextView;
        lateinit var entryCountTextView: TextView;

        init {
            rankTitleTextView = parent.findViewById(R.id.rankTitleText) as TextView
            rankTextView = parent.findViewById(R.id.rankText) as TextView;
            dateTextView = parent.findViewById(R.id.dateText) as TextView;
            entryCountTextView = parent.findViewById(R.id.entryCountText) as TextView;
            parent.rootView.setOnClickListener { v -> mListener.onRankRowClick(v, position) }
        }

        fun setProgram(rank: Int, program: Program) {
            rankTextView.text = rank.toString() + ".";
            rankTitleTextView.text = program.title;
            dateTextView.text = DateFormatter.toDateHmFormat(program.startAt) + " ~ " + DateFormatter.toHmFormat(program.endAt)
            entryCountTextView.text = program.entryCount.toString()
            if (rank <= 3) {
                rankTextView.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.colorPrimary))
            } else {
                rankTextView.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.primary_text_default_material_light))
            }
        }

        companion object {
            fun newInstance(parent: View, listener: OnRankRowClickListener): RankItemViewHolder = RankItemViewHolder(parent, listener)
        }

    }
}