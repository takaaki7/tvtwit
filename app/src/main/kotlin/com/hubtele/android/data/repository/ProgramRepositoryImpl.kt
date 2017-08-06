package com.hubtele.android.data.repository

import com.hubtele.android.Constants
import com.hubtele.android.model.ProgramTable
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.data.repository.datasource.net.ApiServiceImpl
import com.hubtele.android.model.Program
import com.hubtele.android.util.PrefUtil
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProgramRepositoryImpl  @Inject constructor(val apiClient: ApiService) : ProgramRepository {
    override fun search(title: String): Observable<List<Program>> {
        return apiClient.search(title)
                .doOnError(errorLogHandler)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    override fun getProgramTable(): Observable<ProgramTable> {
        return apiClient.programTable().doOnNext {
            PrefUtil.saveDate(Constants.PrefKey.PROGRAM_TABLE_REFRESHED_AT, Date())
        }.doOnError(errorLogHandler)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}