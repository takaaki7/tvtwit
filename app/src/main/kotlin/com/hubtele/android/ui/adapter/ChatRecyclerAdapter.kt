package com.hubtele.android.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener;
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hubtele.android.Constants
import com.hubtele.android.R
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.Entry
import com.hubtele.android.model.EntryType
import com.hubtele.android.util.ScreenUtil
import timber.log.Timber
import java.text.DateFormat
import java.util.*

class ChatRecyclerAdapter(private val mItemList: ArrayList<Entry>, private val mListener: ChatRecyclerAdapter.OnEntryClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val MAX_ARRAY_LENGTH = 6000

    fun add(entry: Entry):Boolean {
        if (entry.replies != null) mItemList.remove(entry)
        mItemList.add(entry)
        return removeHalfIfOver()
    }

    fun removeHalfIfOver():Boolean {
        val size = mItemList.size
        if (size > MAX_ARRAY_LENGTH) {
            Timber.d("REMOVE HALF")
            val foot = size - (MAX_ARRAY_LENGTH/2)
            mItemList.subList(0, foot).clear()
            notifyItemRangeRemoved(0, foot)
            return true
        }
        return false
    }

    fun addAll(entries: List<Entry>) {
        removeHalfIfOver()
        mItemList.addAll(entries)
    };

    interface OnEntryClickListener {
        fun onEntryClick(v: View, entry: Entry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return ChatItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.chat_entry_row, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ChatItemViewHolder).setEntry(mItemList[position])
    }

    fun getItem(position: Int): Entry = mItemList[position]

    override fun getItemCount(): Int = mItemList.size

    class ChatItemViewHolder(val parent: View, val mListener: OnEntryClickListener?) : RecyclerView.ViewHolder(parent) {
        lateinit var rootLayout: LinearLayout
        lateinit var mUserNameText: TextView;
        lateinit var mContentText: TextView;
        lateinit var mDateText: TextView;
        lateinit var mUserIconImage: ImageView;
        lateinit var mTwitterIconImage: ImageView;

        init {
            rootLayout = parent as LinearLayout
            mUserNameText = parent.findViewById(R.id.userNameText) as TextView
            mContentText = parent.findViewById(R.id.contentText) as TextView
            mDateText = parent.findViewById(R.id.dateText) as TextView
            mUserIconImage = parent.findViewById(R.id.userIconImage)as ImageView
            mTwitterIconImage = parent.findViewById(R.id.twitterIconImage)as ImageView
        }

        fun setEntry(entry: Entry) {
            parent.setOnClickListener { v -> mListener?.onEntryClick(v, entry) }
            mUserNameText.text = entry.userName
            mContentText.text = entry.content
            mDateText.text = DateFormatter.toHmsFormat(entry.date)
            Glide.with(parent.context).load(entry.imagePath).into(mUserIconImage)
            if (entry.type == EntryType.T) {
                mUserIconImage.visibility = View.VISIBLE
                mTwitterIconImage.visibility = View.VISIBLE
                Glide.with(parent.context).load(R.drawable.twitter16).into(mTwitterIconImage)
            } else {
                mUserIconImage.visibility = View.GONE
                mTwitterIconImage.visibility = View.GONE
            }
            if (entry.hasReplies()) {
                addReplies(entry.replies!!)
            } else {
                rootLayout.removeView(rootLayout.findViewWithTag(REPLY_LIST_TAG))
            }
        }

        private fun addReplies(replies: List<Entry>) {
            var replyListView: LinearLayout? = rootLayout.findViewWithTag(REPLY_LIST_TAG) as? LinearLayout
            if (replyListView == null) {
                replyListView = LinearLayout(parent.context)
                replyListView.orientation = LinearLayout.VERTICAL
                //                replyListView.layoutParams = LinearLayout.LayoutParams(
                //                        RelativeLayout.LayoutParams.MATCH_PARENT
                //                        , RelativeLayout.LayoutParams.WRAP_CONTENT)
                replyListView.tag = REPLY_LIST_TAG
                var lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                //                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                lp.setMargins(REPLY_LIST_MARGIN_LEFT, REPLY_LIST_MARIN_TOP, 0, 0)
                rootLayout.addView(replyListView, lp)

            }
            for (i in replyListView.childCount..replies.size - 1) {
                val h = ChatItemViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_entry_row, replyListView, false), mListener)
                h.setEntry(replies[i])
                replyListView.addView(h.parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            //            replyListView.requestLayout()
            //                replyList.addView()
            //                rootLayout.addView()
        }

        companion object {
            val REPLY_LIST_MARGIN_LEFT = ScreenUtil.dpToPx(18)
            val REPLY_LIST_MARIN_TOP = ScreenUtil.dpToPx(8)
            val REPLY_LIST_TAG = "reply_list"
        }
    }
}