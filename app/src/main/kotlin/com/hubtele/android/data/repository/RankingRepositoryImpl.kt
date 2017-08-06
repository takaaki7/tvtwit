package com.hubtele.android.data.repository

import com.hubtele.android.model.Program
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.data.repository.datasource.net.ApiServiceImpl
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RankingRepositoryImpl  @Inject constructor(val apiClient: ApiService) : RankingRepository {

    override fun getRanking(startIndex: Int, count: Int): Observable<List<Program>> {
        return apiClient.rankingWeekly(startIndex, count).doOnNext { Timber.d("getRanking:" + it) }
                .doOnError(errorLogHandler)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}