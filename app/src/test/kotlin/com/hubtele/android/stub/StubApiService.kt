package com.hubtele.android.stub

import com.hubtele.android.data.repository.ChatStream
import com.hubtele.android.data.repository.datasource.net.ApiService
import com.hubtele.android.model.*
import com.hubtele.android.util.DateUtil
import retrofit.client.Response
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.observable
import rx.lang.kotlin.subscriber
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by nakama on 2015/11/4.
 */

class StubApiService : ApiService {
    override fun search(title: String): Observable<List<Program>> {
        return observable { subscriber ->
            subscriber.onNext((0..20).map { i ->
                Program(id = "id${i}",
                        title = "title${i}",
                        station = "00",
                        startAt = if (i % 2 == 0) Date() else DateUtil.diffHour(Date(), 2),
                        endAt = if (i % 2 == 0) DateUtil.addHour(Date(), 3) else DateUtil.diffHour(Date(), 1),
                        entryCount = (Math.random() * 2000).toInt())
            })
            subscriber.onCompleted()
        }
    }

    override fun programTable(): Observable<ProgramTable> {
        return observable<ProgramTable> { subscriber ->
            val table = ProgramTable()
            table.set("01", ProgramInfo("NHK", Program(title = "TestProgram", id = "0101", station = "01", entryCount = 0, endAt = Date(), startAt = Date())))
            subscriber.onNext(table)
            subscriber.onCompleted()
        }.delay(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
    }

    override fun rankingWeekly(startIndex: Int, count: Int): Observable<List<Program>> {
        return observable<List<Program>> { subscriber ->
            subscriber.onNext((startIndex..(startIndex + count - 1)).toMutableList()
                    .map {
                        Program(title = (it + 1).toString() + "title", id = "${it + 1}id",
                                station = "0", entryCount = 0, endAt = Date(), startAt = Date())
                    })
        }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun timeShift(programId: String, page: Int): Observable<List<Entry>> {
        Timber.d("timeshift:$programId $page");
        return observable {}
    }

    override fun report(report: Report): Observable<Response> {
        throw UnsupportedOperationException()
    }

}