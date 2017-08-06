package com.hubtele.android.model

class ChatEvent(val eventName: String) {
    lateinit var data: Any;

    constructor(eventName: String, data: String) : this(eventName) {
        this.data = data
    }

    constructor(eventName: String, data: List<Entry>) : this(eventName) {
        this.data = data
    }

    constructor(eventName: String, data: Any) : this(eventName) {
        this.data = data
    }

}