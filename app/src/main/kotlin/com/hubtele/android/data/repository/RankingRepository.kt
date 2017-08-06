package com.hubtele.android.data.repository

import com.hubtele.android.model.Program
import rx.Observable

interface RankingRepository {
    fun getRanking(startIndex: Int,count:Int): Observable<List<Program>>;
}