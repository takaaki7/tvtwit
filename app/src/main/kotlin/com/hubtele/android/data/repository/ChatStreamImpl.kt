package com.hubtele.android.data.repository

import com.hubtele.android.BuildConfig
import com.hubtele.android.model.Entry
import com.hubtele.android.Constants.Chat;
import com.hubtele.android.helper.MyGson
import com.hubtele.android.model.ChatEvent
import com.hubtele.android.model.Post
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.*;
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import rx.lang.kotlin.observable
import rx.lang.kotlin.subscriber
import timber.log.Timber
import java.util.*

class ChatStreamImpl : ChatStream {
    override fun init(programId: String) {
        socket.emit(Chat.INIT, programId);
    }


    override fun emitEntry(post: Post) {
        val o = JSONObject()
        o.put("content", post.content)
        if (post.replyTo != null) o.put("replyTo", post.replyTo)
        Timber.d("emitEntry${post.content}");
        socket.emit(Chat.POST_UPLOAD, o);
    }

    override fun emitReconnected(programId: String) {
        Timber.d("emitReconnect:$programId");
        socket.emit(Chat.RECONNECTED, programId)
    }

    private val socket: Socket = IO.socket(BuildConfig.API_URL)
    private val LISTEN_EVENTS = arrayListOf(
            EVENT_CONNECT, EVENT_DISCONNECT, EVENT_RECONNECT,
            EVENT_MESSAGE, EVENT_RECONNECT_ATTEMPT, EVENT_ERROR,
            Chat.ON_ENTRY_LOG, Chat.ON_ERROR, Chat.ON_PROGRAM_END)

    override fun getEventStream(): Observable<ChatEvent> {
        return observable { subscriber ->
            LISTEN_EVENTS.forEach { eventName ->
                socket.on(eventName, Emitter.Listener { data ->
                    Timber.d("socket event$eventName")
                    when (eventName) {
                        Chat.ON_ENTRY_LOG -> {
                            val logsOrig = data[0] as JSONArray
                            val logs: ArrayList<Entry> = ArrayList();
                            for (i in 0..logsOrig.length() - 1) logs.add(convertToEntry(logsOrig.get(i) as JSONObject))
                            subscriber.onNext(ChatEvent(eventName, logs))
                        }
                        else -> if (data.size == 0) {
                            subscriber.onNext(ChatEvent(eventName))
                        } else {
                            subscriber.onNext(ChatEvent(eventName, data[0]))
                        }
                    }
                })
            }
        }
    }

    override fun getEntryStream(): Observable<Entry> {
        return observable { subscriber ->
            socket.on(Chat.ON_ENTRY, Emitter.Listener {
                subscriber.onNext(convertToEntry(it[0] as JSONObject))
            })
        }
    }

    private fun convertToEntry(json: JSONObject): Entry = MyGson.gson.fromJson((json).toString(), Entry::class.java)

    override fun connect() {
        Timber.d("connect")
        socket.connect()
    }

    override fun disconnect() {
        socket.off()
        socket.close()
        LISTEN_EVENTS.forEach { socket.off(it) }
    }
}