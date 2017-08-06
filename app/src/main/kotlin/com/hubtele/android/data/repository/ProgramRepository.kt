package com.hubtele.android.data.repository

import com.hubtele.android.model.Program
import com.hubtele.android.model.ProgramTable
import rx.Observable

interface ProgramRepository {
    fun getProgramTable(): Observable<ProgramTable>;
    fun search(title: String): Observable<List<Program>>;
}