package com.hubtele.android.internal.di.module

import com.hubtele.android.data.repository.*
import com.hubtele.android.data.repository.datasource.net.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RestRepositoryModule {
    @Provides
    fun provideProgramRepository(api: ApiService): ProgramRepository {
        return ProgramRepositoryImpl(api);
    }

    @Provides
    fun provideRankingRepository(api: ApiService): RankingRepository {
        return RankingRepositoryImpl(api);
    }

    @Provides
    fun provideEntryRepository(api: ApiService): EntryRepository {
        return EntryRepositoryImpl(api);
    }

}