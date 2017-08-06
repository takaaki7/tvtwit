package com.hubtele.android

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.manager.ChatManager
import com.hubtele.android.model.ChatEvent
import com.hubtele.android.model.Entry
import com.hubtele.android.model.EntryType
import com.hubtele.android.model.Post
import io.socket.client.Socket
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by nakama on 2015/10/31.
 */
class ChatManagerTest {

    var chatManager: ChatManager = ChatManager(ChatManagerTest.StubChatStream())
    @Before
    fun setup() {
        //        ChatComponentBuilder.init(StubChatStreamModule("testProgramId")).inject(chatManager)
    }

    @Test
    fun onEntryLogIsCome() {
        chatManager.onCreate("testId")
        chatManager.getEntryStream().subscribe {
            System.out.println("entry:$it");
            assertTrue(true)
        }
        //        assertTrue(true)
    }

    @Test
    fun disconnectIsCalledWhenProgramIsAlreadyEndedAndReconnectAttempt() {

    }

    @Test
    fun entryIsBufferedAndSendedWhenConnected() {
        chatManager.onCreate("testId")

    }


    /**
     * Created by nakama on 2015/10/31.
     */
    class StubChatStream : ChatStream {
        var eventStreamSubject: PublishSubject<ChatEvent> = PublishSubject<ChatEvent>()
        var connected = false
        var disconnected = false
        var index = 0
        override fun getEntryStream(): Observable<Entry> {
            return Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .filter { msec -> msec < 2 }
                    .map { long ->
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
                    }
        }

        override fun getEventStream(): Observable<ChatEvent> {
            return eventStreamSubject
        }

        override fun connect() {
            connected = true
            eventStreamSubject.onNext(ChatEvent(Socket.EVENT_CONNECT))
            Timber.d("connect stub")
        }

        override fun disconnect() {
            disconnected = true
        }

        override fun init(programId: String) {
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
            eventStreamSubject.onNext(ChatEvent(Constants.Chat.ON_ENTRY_LOG, logs))
            Timber.d("init stub:$programId")
        }


        override fun emitEntry(post: Post) {
            Timber.d("emitEntry stub:$post")
        }

        override fun emitReconnected(programId: String) {
            Timber.d("emitReconnected stub:$programId")
        }

    }
}