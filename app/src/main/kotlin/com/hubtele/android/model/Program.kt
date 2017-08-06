package com.hubtele.android.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.util.*

/**
 * Created by nakama on 2015/10/23.
 */
@Parcel
class Program {
    lateinit var id: String
    lateinit var station: String
    lateinit var title: String
    @SerializedName("start_at")
    lateinit var startAt: Date
    @SerializedName("end_at")
    lateinit var endAt: Date
    var entryCount: Int = 0

    constructor() {
        /*empty constructor for parceler*/
    }

    constructor(id: String,
                station: String,
                title: String,
                startAt: Date,
                endAt: Date,
                entryCount: Int) {
        this.id = id
        this.station = station
        this.title = title
        this.startAt = startAt
        this.endAt = endAt
        this.entryCount = entryCount
    }

    override fun toString(): String {
        return "[" +
                "id:$id ," +
                "station:$station ," +
                "title:$title ," +
                "startAt:$startAt ," +
                "endAt:$endAt ," +
                "entryCount:$entryCount ," +
                "]"
    }

    override fun equals(other: Any?): Boolean {
        return id.equals((other as Program).id)
    }
}