package com.hubtele.android.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hubtele.android.Constants
import com.hubtele.android.R
import kotlinx.android.synthetic.main.activity_web_view.*
import timber.log.Timber

class WebViewActivity : BaseActivity() {
    override fun getPageName(): String? = intent.getStringExtra(Constants.IntentExtraKey.WEB_GA_PAGE)

    override val contentViewId: Int = R.layout.activity_web_view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                val intent = Intent(Intent.ACTION_VIEW)
                var uri: Uri;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    uri = Uri.parse("http://" + url)
                } else {
                    uri = Uri.parse(url)
                }
                intent.setData(uri)
                Timber.d("url:$url,uri.path:" + uri.path);
                if (uri.path.equals("/")) {
                    finish()
                } else {
                    startActivity(intent);
                }
                return true;
            }

            override fun onLoadResource(view: WebView?, url: String) {
            }
        });
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = intent.getStringExtra(Constants.IntentExtraKey.TITLE)
        webView.loadUrl(intent.getStringExtra(Constants.IntentExtraKey.WEB_URL));
        webView.settings.javaScriptEnabled = true;
    }

    override fun onDestroy() {
        super.onDestroy()
        webViewRoot.removeAllViews()
        webView.destroy()
    }
}