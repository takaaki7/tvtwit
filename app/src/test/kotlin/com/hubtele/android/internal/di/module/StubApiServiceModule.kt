package com.hubtele.android.internal.di.module

import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.stub.StubApiService
import dagger.Module
import dagger.Provides

/**
 * Created by nakama on 2015/11/04.
 */
@Module
class StubApiServiceModule : RestModule() {
    override fun provideApiService(): ApiService {
        return StubApiService()
    }
}