package com.hubtele.android.ui.helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.hubtele.android.Constants
import com.hubtele.android.model.Program
import com.hubtele.android.ui.activity.ChatActivity
import com.hubtele.android.ui.activity.TimeShiftActivity
import org.parceler.Parcels
import java.util.*

object ActivityHelper {

    fun goBoardActivity(activity: Activity, program: Program) {
        val intent = Intent(activity,
                if (program.endAt < Date())
                    TimeShiftActivity::class.java
                else ChatActivity::class.java)
        intent.putExtra(Constants.IntentExtraKey.PROGRAM, Parcels.wrap(program))
        activity.startActivityForResult(intent, Constants.Chat.BOARD_ACTIVITY_REQUEST_CODE)
    }

    fun goGooglePlayForReview(currentActivity: Activity) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("market://details?id=${currentActivity.packageName}"))
        currentActivity.startActivity(intent)
    }
}
