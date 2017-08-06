package com.hubtele.android.data.repository

import rx.functions.Action1
import timber.log.Timber

var errorLogHandler = Action1 { e: Throwable -> Timber.e(e.message); }