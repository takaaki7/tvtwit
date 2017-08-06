package com.hubtele.android.ui.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.GridView
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.Program
import com.hubtele.android.model.ProgramInfo
import com.hubtele.android.util.ScreenUtil
import kotlinx.android.synthetic.main.card_program.view.*
import kotlinx.android.synthetic.main.card_program.*
import java.util.*

class ProgramTableAdapter(private val dataList: ArrayList<ProgramInfo>, private val listener: ProgramTableAdapter.OnCardClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun add(p: ProgramInfo) = dataList.add(p)
    fun getItem(position: Int) = dataList[position]
    fun clear() = dataList.clear()
    interface OnCardClickListener {
        fun onClicked(info: ProgramInfo)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProgramTableViewHolder).setProgram(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return ProgramTableViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_program, parent, false), listener)
    }

    class ProgramTableViewHolder(val parent: View, val listener: OnCardClickListener) : RecyclerView.ViewHolder(parent) {
        var programInfo: ProgramInfo? = null;
        fun setProgram(programInfo: ProgramInfo) {
            this.programInfo = programInfo
            parent.stationNameText.text = programInfo.name;
            parent.programTitleText.text = programInfo.program.title;
            parent.programTimeText.text = DateFormatter.toHmFormat(programInfo.program.startAt) +
                    "~" + DateFormatter.toHmFormat(programInfo.program.endAt);
            parent.entryCountText.text = programInfo.program.entryCount.toString();
            parent.innerClickableLayout.setOnClickListener { listener.onClicked(this.programInfo!!) }
        }
    }

}