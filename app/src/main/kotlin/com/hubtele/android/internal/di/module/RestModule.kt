package com.hubtele.android.internal.di.module

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.data.repository.ChatStreamImpl
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.data.repository.datasource.net.ApiServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RestModule {
    @Singleton
    @Provides
    open fun provideApiService(): ApiService {
        return ApiServiceImpl.apiClient
    }
}
