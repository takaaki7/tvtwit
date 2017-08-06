package com.hubtele.android.ui.helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.hubtele.android.BuildConfig
import com.hubtele.android.Constants
import com.hubtele.android.util.PrefUtil
import timber.log.Timber
import java.util.*

open class ReviewDialogHelper {

    private val MSEC_SHOW_BORDER = 600 * 1000//600000

    private val COUNT_SEE_ACHIEVE_CRITERIA = 2

    private val TEXT = "いつもご利用ありがとうございます。レビューにご協力いただけませんか？"

    fun showDialogIfTiming(activity: Activity, chatCreatedAt: Long) {
        Timber.d("showDialog sec${chatCreatedAt}")
        if ((Date().time - chatCreatedAt ) > MSEC_SHOW_BORDER) {
            var count = PrefUtil.loadInt(Constants.PrefKey.COUNT_SEE_ACHIEVE)
            if (count < 100) {
                if (count == -1) count = 0
                PrefUtil.saveInt(Constants.PrefKey.COUNT_SEE_ACHIEVE, ++count)
            }
            var reviewedVersion = PrefUtil.loadInt(Constants.PrefKey.REVIEWED_VERSION)
            Timber.d("showDialog count:${count},reviewedVersion]${reviewedVersion},shall:${!PrefUtil.loadBooleanDefFalse(Constants.PrefKey.SHALL_NEVER_REVIEW)}")
            if (count >= COUNT_SEE_ACHIEVE_CRITERIA
                    && reviewedVersion !== BuildConfig.VERSION_CODE
                    && !PrefUtil.loadBooleanDefFalse(Constants.PrefKey.SHALL_NEVER_REVIEW) ) {
                showDialog(activity)
            }
        }
    }

    public open fun showDialog(activity: Activity) {
        MaterialDialog.Builder(activity)
                .title("レビューのお願い")
                .content(TEXT)
                .negativeText("今はしない")
                .neutralText("絶対にしない")
                .positiveText("評価する")
                .onNegative { dialog, action -> dialog.dismiss() }
                .onNeutral { dialog, action ->
                    PrefUtil.saveBoolean(Constants.PrefKey.SHALL_NEVER_REVIEW, true)
                }
                .onPositive { dialog, action ->
                    PrefUtil.saveInt(Constants.PrefKey.REVIEWED_VERSION, BuildConfig.VERSION_CODE);
                    ActivityHelper.goGooglePlayForReview(activity)  }
                .show();
    }
}