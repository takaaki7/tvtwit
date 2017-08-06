package com.hubtele.android.internal.di.component

import com.hubtele.android.internal.di.module.RestRepositoryModule
import com.hubtele.android.internal.di.scope.FragmentScope
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.data.repository.ProgramRepository
import com.hubtele.android.data.repository.RankingRepository
import com.hubtele.android.ui.activity.ChatActivity
import com.hubtele.android.ui.activity.TimeShiftActivity
import com.hubtele.android.ui.fragment.ProgramTableFragment
import com.hubtele.android.ui.fragment.RankingFragment
import com.hubtele.android.ui.fragment.SearchFragment
import dagger.Component
import javax.inject.Singleton

@FragmentScope
@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(RestRepositoryModule::class))
interface SceneComponent {
    fun inject(a: ProgramTableFragment)
    fun inject(a: RankingFragment)
    fun inject(a: SearchFragment)
    fun inject(a:TimeShiftActivity)
    fun inject(a:ChatActivity)
    fun getIProgramService(): ProgramRepository;
    fun getIRankingService(): RankingRepository;
    fun getApiService(): ApiService;
}