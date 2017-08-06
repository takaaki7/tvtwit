package com.hubtele.android.internal.di.component

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.internal.di.module.RestModule
import com.hubtele.android.internal.di.module.RestRepositoryModule
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.data.repository.ProgramRepository
import com.hubtele.android.data.repository.RankingRepository
import com.hubtele.android.internal.di.module.ChatStreamModule
import com.hubtele.android.ui.activity.BaseActivity
import com.hubtele.android.ui.fragment.BaseFragment
import com.hubtele.android.ui.fragment.ProgramTableFragment
import dagger.Component

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(RestModule::class, ChatStreamModule::class))
interface AppComponent {
    fun inject(a: BaseActivity)
//    fun plus(r:RestRepositoryModule):FragmentComponent
    fun getApiService(): ApiService;
    fun getChatStream(): ChatStream;
  }
