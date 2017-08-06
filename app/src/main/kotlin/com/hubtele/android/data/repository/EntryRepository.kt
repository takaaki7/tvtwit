package com.hubtele.android.data.repository

import com.hubtele.android.model.Entry
import retrofit.http.GET
import rx.Observable

interface EntryRepository {
    fun timeShiftEntry(programId: String, page: Int): Observable<List<Entry>>
}

