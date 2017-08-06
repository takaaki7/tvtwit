package com.hubtele.android.data.repository

import com.hubtele.android.model.ChatEvent
import com.hubtele.android.model.Entry
import com.hubtele.android.model.Post
import rx.Observable

interface ChatStream {
    fun getEntryStream(): Observable<Entry>
    fun getEventStream(): Observable<ChatEvent>
    fun connect()
    fun disconnect()
    fun init(programId:String)
    fun emitEntry(post: Post)
    fun emitReconnected(programId:String)
}