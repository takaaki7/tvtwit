package com.hubtele.android.manager

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.model.ChatEvent
import com.hubtele.android.model.Entry
import com.hubtele.android.Constants.Chat;
import com.hubtele.android.model.Post
import io.socket.client.Socket.*;
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChatManager public @Inject constructor(val chatStream: ChatStream) {
    private var connectedOnce = false
    private var programIsEnd = false;
    private var connecting = false;
    private lateinit var programId: String;

    //if user emit entry when not connecting, save it to buffer and send when connected.
    private var buffer = ArrayList<Post>();

    fun getEntryStream(): Observable<Entry> = chatStream.getEntryStream().observeOn(AndroidSchedulers.mainThread())

    fun getEventStream(): Observable<ChatEvent> {
        return chatStream.getEventStream()
                .doOnNext {
                    when (it.eventName) {
                        EVENT_CONNECT -> {
                            connecting = true;
                            if (!connectedOnce) {
                                connectedOnce = true;
                                chatStream.init(programId)
                            } else {
                                chatStream.emitReconnected(programId);
                            }
                            buffer.forEach { chatStream.emitEntry(it) }
                            buffer.clear()
                        }
                        EVENT_RECONNECT_ATTEMPT -> if (programIsEnd) chatStream.disconnect()
                        EVENT_DISCONNECT -> {
                            connecting = false
                        };
                        EVENT_ERROR -> Timber.e(it.data.toString());
                        Chat.ON_ERROR -> Timber.e(it.data.toString());
                        Chat.ON_PROGRAM_END -> programIsEnd = true;
                    }
                }
                .doOnError({ e -> Timber.e(e.message); })
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun emitEntry(post: Post) {
        if (connecting) {
            chatStream.emitEntry(post)
        } else {
            buffer.add(post);
        }

    }

    fun isProgramEnd() = programIsEnd

    fun onCreate(pid: String) {
        programId = pid;
        chatStream.connect()
    }

    fun onDestroy() = chatStream.disconnect()
}