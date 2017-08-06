package com.hubtele.android.model

data class Post(val content: String, val replyTo: String?) {
    override fun toString(): String = "{content:$content,replyTo:$replyTo}"
}