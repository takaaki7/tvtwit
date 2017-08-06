package com.hubtele.android.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Entry(
        @SerializedName("_id")
        val id: String,
        val content: String,
        val userId: String?,
        val userName: String,
        val programId: String,
        val replyTo: String?,
        var replies: List<Entry>?,
        val date: Date,
        val screenName: String?,
        val imagePath: String?,
        val type: EntryType
) {
    override fun equals(other: Any?): Boolean {
        return id.equals((other as Entry).id)
    }

    fun hasReplies(): Boolean = replies != null && replies!!.size > 0
}