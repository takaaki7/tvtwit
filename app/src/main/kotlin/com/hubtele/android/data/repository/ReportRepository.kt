package com.hubtele.android.data.repository

import com.hubtele.android.model.Report
import retrofit.client.Response
import rx.Observable

interface ReportRepository {
    fun report(report: Report): Observable<Response>;
}