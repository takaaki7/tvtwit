package com.hubtele.android.data.repository

import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.model.Entry
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EntryRepositoryImpl  @Inject constructor(val apiClient: ApiService) : EntryRepository {
    override fun timeShiftEntry(programId: String, page: Int): Observable<List<Entry>> {
        return apiClient.timeShift(programId, page)
                .doOnError(errorLogHandler)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}