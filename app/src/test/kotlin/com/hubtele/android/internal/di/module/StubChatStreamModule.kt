package com.hubtele.android.internal.di.module

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.stub.StubChatStream
import dagger.Module
import dagger.Provides

/**
 * Created by nakama on 2015/10/31.
 */
@Module
class StubChatStreamModule() : ChatStreamModule() {
    override fun provideChatStream(): ChatStream {
        return StubChatStream()
    }
}