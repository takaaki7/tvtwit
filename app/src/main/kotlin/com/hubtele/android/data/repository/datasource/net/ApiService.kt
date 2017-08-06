package com.hubtele.android.data.repository.datasource.net

import com.hubtele.android.model.Entry
import com.hubtele.android.model.Program
import com.hubtele.android.model.ProgramTable
import com.hubtele.android.model.Report
import retrofit.client.Response
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST
import retrofit.http.Query
import rx.Observable

interface ApiService {
    @GET("/api/program_table")
    fun programTable(): Observable<ProgramTable>

    @GET("/api/ranking_weekly")
    fun rankingWeekly(@Query("startIndex") startIndex: Int, @Query("count") count: Int): Observable<List<Program>>

    @GET("/api/timeshift")
    fun timeShift(@Query("program") programID: String, @Query("page") page: Int): Observable<List<Entry>>

    @GET("/api/search")
    fun search(@Query("title") title: String): Observable<List<Program>>

    @POST("/api/report")
    fun report(@Body report: Report): Observable<Response>

}
