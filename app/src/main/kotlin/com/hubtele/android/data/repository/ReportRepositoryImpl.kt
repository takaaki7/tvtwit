package com.hubtele.android.data.repository

import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.model.Report
import retrofit.client.Response
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ReportRepositoryImpl  @Inject constructor(val apiClient: ApiService) : ReportRepository {
    override fun report(report: Report): Observable<Response> {
        return apiClient.report(report).doOnError(errorLogHandler)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}