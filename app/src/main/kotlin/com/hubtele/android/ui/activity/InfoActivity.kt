package com.hubtele.android.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import com.hubtele.android.BuildConfig
import com.hubtele.android.Constants
import com.hubtele.android.R
import com.hubtele.android.ui.helper.ActivityHelper
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import kotlinx.android.synthetic.main.activity_info.*;

class InfoActivity : BaseActivity() {
    override fun getPageName(): String? = "info"

    override val contentViewId: Int = R.layout.activity_info;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = getString(R.string.info_title)
        aboutRow.setOnClickListener { startWebViewActivity(Constants.URL.ABOUT, aboutText.text.toString(),"about") }
        inquiryRow.setOnClickListener { startWebViewActivity(Constants.URL.INQUIRY, inquiryText.text.toString(),"inquiry") }
        reviewRow.setOnClickListener { ActivityHelper.goGooglePlayForReview(this) }
        licenseRow.setOnClickListener {
            LibsBuilder()
                    .withActivityTitle(getString(R.string.info_title))
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTheme(R.style.AppTheme)
                    .start(this);
        }
        versionText.text = "バージョン " + BuildConfig.VERSION_NAME
    }

    private fun startWebViewActivity(url: String, title: String,pageName:String) {
        var intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(Constants.IntentExtraKey.WEB_URL, url)
        intent.putExtra(Constants.IntentExtraKey.TITLE, title)
        intent.putExtra(Constants.IntentExtraKey.WEB_GA_PAGE,pageName)
        startActivity(intent);
    }
}