package com.hubtele.android.stub

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.model.ChatEvent
import com.hubtele.android.model.Entry
import com.hubtele.android.model.Post
import junit.framework.Test
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.lang.kotlin.TestSubject
import rx.lang.kotlin.observable
import rx.schedulers.TestScheduler
import rx.subjects.PublishSubject
import rx.subjects.Subject
import timber.log.Timber
import io.socket.client.Socket.*;
import java.util.*
import kotlin.concurrent.schedule
import com.hubtele.android.Constants.Chat
import com.hubtele.android.model.EntryType
import java.util.concurrent.TimeUnit

/**
 * Created by nakama on 2015/10/31.
 */
class StubChatStream : ChatStream {
    var eventStreamSubject: PublishSubject<ChatEvent> = PublishSubject<ChatEvent>()
    var connected = false
    var disconnected = false
    var index = 0
    override fun getEntryStream(): Observable<Entry> {
        var first = false
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .filter { msec -> msec < 2 }
                .map { long ->
                    index++
                    if (!first) {
                        first = true
                        Entry(id = "idfromEntryStream${long}",
                                userId = "userId",
                                userName = "userName",
                                content = "content$index",
                                programId = "testProgram",
                                date = Date(),
                                type = EntryType.P,
                                screenName = null,
                                replyTo = null,
                                replies = null,
                                imagePath = null)
                    } else {
                        Entry(id = "idFromLog20",
                                userId = "user20",
                                userName = "user20",
                                content = "content20",
                                programId = "testProgram",
                                date = Date(),
                                type = EntryType.P,
                                screenName = null,
                                replyTo = null,
                                replies = createReplies(),
                                imagePath = null)
                    }
                }
    }

    fun createReplies(): List<Entry> {
        return listOf(Entry(id = "idRepTo20",
                userId = "userRepTo20",
                userName = "userRepTo20",
                content = "contentRepTo20",
                programId = "testProgram",
                date = Date(),
                type = EntryType.P,
                screenName = null,
                replyTo = "idFromLog20",
                replies = null,
                imagePath = null))
    }

    override fun getEventStream(): Observable<ChatEvent> {
        return eventStreamSubject
    }

    override fun connect() {
        //        Timer().schedule(200, {
        connected = true
        eventStreamSubject.onNext(ChatEvent(EVENT_CONNECT))
        //        })
        Timber.d("connect stub")
    }

    override fun disconnect() {
        disconnected = true
        Timber.d("disconnect stub")
    }

    override fun init(programId: String) {
        //        Timer().schedule(200, {
        var logs = (0..40).map { i ->
            index++
            Entry(id = "idFromLog$i",
                    userId = "user$i",
                    userName = "user$i",
                    content = "content$i",
                    programId = "testProgram",
                    date = Date(),
                    type = EntryType.P,
                    screenName = null,
                    replyTo = null,
                    replies = null,
                    imagePath = null)
        }
        eventStreamSubject.onNext(ChatEvent(Chat.ON_ENTRY_LOG, logs + createEntryWithReplies()))
        //        })
        Timber.d("init stub:$programId")
    }

    private fun createEntryWithReplies(): Entry {
        index++
        val replies = listOf(Entry(
                id = "idRep$index",
                userId = "userRep$index",
                userName = "userNameR",
                content = "replyToParent",
                programId = "testProgram",
                date = Date(),
                type = EntryType.P,
                screenName = null,
                replyTo = "id$index",
                replies = null,
                imagePath = null),
                Entry(
                        id = "idRep2$index",
                        userId = "userRep2$index",
                        userName = "userNameR2",
                        content = "replyToParent2",
                        programId = "testProgram",
                        date = Date(),
                        type = EntryType.P,
                        screenName = null,
                        replyTo = "id$index",
                        replies = null,
                        imagePath = null)
        )
        return Entry(id = "id$index",
                userId = "user$index",
                userName = "user$index",
                content = "content with reply$index",
                programId = "testProgram",
                date = Date(),
                type = EntryType.P,
                screenName = null,
                replyTo = null,
                replies = replies,
                imagePath = null)
    }

    override fun emitEntry(post: Post) {
        Timber.d("emitEntry stub:$post")
    }

    override fun emitReconnected(programId: String) {
        Timber.d("emitReconnected stub:$programId")
    }

}