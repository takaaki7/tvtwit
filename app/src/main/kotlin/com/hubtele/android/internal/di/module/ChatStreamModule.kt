package com.hubtele.android.internal.di.module

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.data.repository.ChatStreamImpl
import dagger.Module
import dagger.Provides

@Module
open class ChatStreamModule() {
    @Provides
    open fun provideChatStream(): ChatStream {
        return ChatStreamImpl()
    }
}